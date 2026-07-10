# CA 公钥验证功能说明

**创建日期：** 2026-04-13  
**功能：** 证书验证和 CA 公钥获取

---

## 📋 CA 密钥架构说明

### 密钥对类型

**当前的 CA 密钥对是系统（CA 机构）的根密钥，不是用户的密钥对。**

```
CA 系统密钥对
├── CA 私钥 (PrivateKey.pem)
│   - 用途：用于签发和签署证书
│   - 保管：存储在 CA 系统中，严格保密
│   - 所有者：CA 机构
│
└── CA 公钥 (PublicKey.pem)
    - 用途：用于验证证书签名
    - 分发：可公开获取
    - 所有者：CA 机构
```

### 证书签发流程

```
1. 用户申请证书
   ↓
2. CA 系统审核申请信息
   ↓
3. CA 系统使用 CA 私钥签署证书
   ↓
4. 用户获得已签名的证书
```

### 证书验证流程

```
1. 获取 CA 公钥（公开）
   ↓
2. 使用 CA 公钥验证证书上的签名
   ↓
3. 如果验证通过 → 证书由该 CA 签发
   ↓
4. 如果验证失败 → 证书不是该 CA 签发或已损坏
```

---

## ✅ 新增功能

### 1. 证书验证 API

**接口：** `POST /api/certificates/verify`

**功能：** 上传证书文件，验证是否由本 CA 签发

**请求：**
```http
POST /api/certificates/verify
Content-Type: multipart/form-data

file: certificate.cer
```

**响应：**
```json
{
  "code": 200,
  "message": "证书验证完成",
  "data": {
    "valid": true,
    "message": "证书验证通过，由本 CA 签发",
    "serialNumber": "1A2B3C4D5E6F",
    "subject": "CN=Example Inc,O=Example",
    "issuer": "CN=CA System,O=CA",
    "notBefore": "2024-01-01T00:00:00Z",
    "notAfter": "2025-01-01T00:00:00Z",
    "expired": false,
    "notYetValid": false,
    "status": "有效"
  }
}
```

**验证结果说明：**

| 字段 | 说明 |
|------|------|
| `valid` | true: 验证通过，false: 验证失败 |
| `message` | 验证结果描述 |
| `serialNumber` | 证书序列号 |
| `subject` | 证书持有者信息 |
| `issuer` | 证书颁发者（CA）信息 |
| `notBefore` | 有效期开始时间 |
| `notAfter` | 有效期结束时间 |
| `expired` | 是否已过期 |
| `notYetValid` | 是否尚未生效 |
| `status` | 证书状态：有效/已过期/尚未生效 |

### 2. CA 公钥获取 API

**接口：** `GET /api/certificates/public-key`

**功能：** 获取 CA 公钥的 PEM 格式文本

**请求：**
```http
GET /api/certificates/public-key
```

**响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "publicKey": "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA...\n-----END PUBLIC KEY-----\n",
    "algorithm": "RSA",
    "keySize": "2048",
    "usage": "用于验证由本 CA 签发的证书签名"
  }
}
```

---

## 🔧 技术实现

### 后端实现

#### 1. CertificateController 新增接口

```java
// 证书验证接口
@PostMapping("/verify")
public ResponseEntity<ApiResponse<Map<String, Object>>> verifyCertificate(
        @RequestParam("file") MultipartFile certFile) {
    // 1. 读取证书文件
    byte[] certBytes = certFile.getBytes();
    
    // 2. 使用 CA 公钥验证证书签名
    boolean isValid = certificateManagementService.verifyCertificateSignature(certBytes);
    
    // 3. 解析证书信息
    X509Certificate x509Cert = (X509Certificate) CertificateFactory
        .getInstance("X.509")
        .generateCertificate(new ByteArrayInputStream(certBytes));
    
    // 4. 返回验证结果
    Map<String, Object> result = new HashMap<>();
    result.put("valid", isValid);
    result.put("serialNumber", x509Cert.getSerialNumber().toString(16).toUpperCase());
    result.put("subject", x509Cert.getSubjectX500Principal().getName());
    result.put("issuer", x509Cert.getIssuerX500Principal().getName());
    result.put("notBefore", x509Cert.getNotBefore());
    result.put("notAfter", x509Cert.getNotAfter());
    
    // 5. 检查有效期
    Date now = new Date();
    boolean isExpired = x509Cert.getNotAfter().before(now);
    boolean isNotYetValid = x509Cert.getNotBefore().after(now);
    result.put("expired", isExpired);
    result.put("notYetValid", isNotYetValid);
    result.put("status", isExpired ? "已过期" : (isNotYetValid ? "尚未生效" : "有效"));
    
    return ResponseEntity.ok(ApiResponse.success("证书验证完成", result));
}

// 获取 CA 公钥接口
@GetMapping("/public-key")
public ResponseEntity<ApiResponse<Map<String, String>>> getCAPublicKey() {
    String publicKeyPem = certificateManagementService.getCAPublicKeyInPem();
    
    Map<String, String> result = new HashMap<>();
    result.put("publicKey", publicKeyPem);
    result.put("algorithm", "RSA");
    result.put("keySize", "2048");
    result.put("usage", "用于验证由本 CA 签发的证书签名");
    
    return ResponseEntity.ok(ApiResponse.success(result));
}
```

#### 2. CertificateManagementService 新增方法

```java
/**
 * 验证证书签名 - 使用 CA 公钥验证证书是否由本 CA 签发
 */
public boolean verifyCertificateSignature(byte[] certBytes) throws Exception {
    try {
        // 加载证书
        X509Certificate cert = loadCertificate(certBytes);
        
        // 获取 CA 公钥
        PublicKey caPublicKey = keyManager.getPublicKey();
        if (caPublicKey == null) {
            log.error("CA 公钥未加载");
            return false;
        }
        
        // 验证证书签名
        cert.verify(caPublicKey);
        
        log.info("证书 {} 验证通过", cert.getSerialNumber().toString(16).toUpperCase());
        return true;
        
    } catch (Exception e) {
        log.error("证书验证失败：{}", e.getMessage());
        return false;
    }
}

/**
 * 获取 CA 公钥的 PEM 格式字符串
 */
public String getCAPublicKeyInPem() throws IOException {
    PublicKey publicKey = keyManager.getPublicKey();
    if (publicKey == null) {
        // 如果内存中没有，尝试从文件读取
        String keyDirectory = System.getenv("KEY_DIRECTORY");
        if (keyDirectory == null) {
            keyDirectory = "C:/keys";
        }
        String publicKeyPath = keyDirectory + "/PublicKey.pem";
        return new String(Files.readAllBytes(Paths.get(publicKeyPath)));
    }
    
    // 将公钥转换为 PEM 格式
    byte[] encoded = publicKey.getEncoded();
    String base64 = Base64.getEncoder().encodeToString(encoded);
    
    StringBuilder pem = new StringBuilder();
    pem.append("-----BEGIN PUBLIC KEY-----\n");
    for (int i = 0; i < base64.length(); i += 64) {
        int end = Math.min(i + 64, base64.length());
        pem.append(base64.substring(i, end)).append("\n");
    }
    pem.append("-----END PUBLIC KEY-----\n");
    
    return pem.toString();
}
```

---

## 🧪 使用示例

### 示例 1：验证证书

**使用 curl 命令：**
```bash
curl -X POST http://localhost:8088/api/certificates/verify \
  -F "file=@certificate.cer"
```

**使用 JavaScript：**
```javascript
const formData = new FormData();
formData.append('file', certificateFile);

fetch('http://localhost:8088/api/certificates/verify', {
  method: 'POST',
  body: formData
})
.then(response => response.json())
.then(data => {
  console.log('验证结果:', data.data);
  if (data.data.valid) {
    console.log('✅ 证书验证通过，由本 CA 签发');
  } else {
    console.log('❌ 验证失败，证书不是由本 CA 签发');
  }
});
```

### 示例 2：获取 CA 公钥

**使用 curl 命令：**
```bash
curl -X GET http://localhost:8088/api/certificates/public-key
```

**使用 JavaScript：**
```javascript
fetch('http://localhost:8088/api/certificates/public-key')
  .then(response => response.json())
  .then(data => {
    console.log('CA 公钥:', data.data.publicKey);
    
    // 保存公钥到文件
    const blob = new Blob([data.data.publicKey], { type: 'text/plain' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'ca_public_key.pem';
    a.click();
  });
```

### 示例 3：使用 OpenSSL 验证证书

**步骤 1：获取 CA 公钥**
```bash
curl -s http://localhost:8088/api/certificates/public-key \
  | jq -r '.data.publicKey' > ca_public_key.pem
```

**步骤 2：使用 OpenSSL 验证证书**
```bash
openssl verify -CAfile ca_public_key.pem certificate.cer
```

**预期输出：**
```
certificate.cer: OK
```

---

## 🔐 安全性说明

### 验证原理

**数字签名验证：**
1. CA 使用私钥对证书进行签名
2. 任何人都可以使用 CA 公钥验证签名
3. 如果验证通过，证明：
   - 证书确实由该 CA 签发
   - 证书内容未被篡改

### 公钥分发

**CA 公钥可以：**
- ✅ 公开发布在网站上
- ✅ 提供给任何需要验证证书的人
- ✅ 集成到客户端应用中
- ✅ 导入到浏览器或操作系统

**CA 私钥必须：**
- 🔒 严格保密，存储在安全的地方
- 🔒 仅限授权人员访问
- 🔒 定期备份
- 🔒 考虑使用 HSM（硬件安全模块）

### 验证场景

**适用场景：**
- ✅ 验证证书是否由本 CA 签发
- ✅ 验证证书是否被篡改
- ✅ 第三方验证证书真伪
- ✅ 审计和合规检查

**不适用场景：**
- ❌ 验证证书持有者身份（需要其他验证机制）
- ❌ 验证证书是否被吊销（需要查询 CRL 或 OCSP）
- ❌ 验证证书是否在有效期内（API 会返回有效期信息，但需要客户端检查）

---

## 📊 验证结果解读

### 验证通过（valid: true）

```json
{
  "valid": true,
  "message": "证书验证通过，由本 CA 签发",
  "status": "有效"
}
```

**含义：**
- ✅ 证书上的数字签名有效
- ✅ 证书由本 CA 签发
- ✅ 证书内容未被篡改
- ✅ 证书在有效期内

### 验证失败（valid: false）

```json
{
  "valid": false,
  "message": "验证失败，证书不是由本 CA 签发或签名已损坏"
}
```

**可能原因：**
1. ❌ 证书不是由本 CA 签发（可能是其他 CA 签发的）
2. ❌ 证书被篡改（内容被修改）
3. ❌ 证书文件损坏
4. ❌ 使用了错误的 CA 公钥验证

### 证书已过期

```json
{
  "valid": true,
  "expired": true,
  "status": "已过期"
}
```

**含义：**
- ✅ 签名验证通过（证书确实由本 CA 签发）
- ⚠️ 但证书已过期，不再有效

### 证书尚未生效

```json
{
  "valid": true,
  "notYetValid": true,
  "status": "尚未生效"
}
```

**含义：**
- ✅ 签名验证通过
- ⚠️ 但证书还未到生效时间

---

## 🛠️ 前端集成建议

### Vue.js 示例

```vue
<template>
  <div>
    <input type="file" @change="handleFileChange" accept=".cer,.crt,.pem" />
    <button @click="verifyCertificate">验证证书</button>
    
    <div v-if="verifyResult">
      <h3>验证结果</h3>
      <p>状态：{{ verifyResult.valid ? '✅ 通过' : '❌ 失败' }}</p>
      <p>消息：{{ verifyResult.message }}</p>
      <p>序列号：{{ verifyResult.serialNumber }}</p>
      <p>持有者：{{ verifyResult.subject }}</p>
      <p>颁发者：{{ verifyResult.issuer }}</p>
      <p>有效期：{{ verifyResult.notBefore }} 至 {{ verifyResult.notAfter }}</p>
      <p>状态：{{ verifyResult.status }}</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      certificateFile: null,
      verifyResult: null
    };
  },
  methods: {
    handleFileChange(event) {
      this.certificateFile = event.target.files[0];
    },
    async verifyCertificate() {
      if (!this.certificateFile) {
        alert('请选择证书文件');
        return;
      }
      
      const formData = new FormData();
      formData.append('file', this.certificateFile);
      
      try {
        const response = await fetch('/api/certificates/verify', {
          method: 'POST',
          body: formData
        });
        
        const result = await response.json();
        this.verifyResult = result.data;
        
      } catch (error) {
        console.error('验证失败', error);
        alert('验证失败：' + error.message);
      }
    }
  }
};
</script>
```

### React 示例

```jsx
import React, { useState } from 'react';

function CertificateVerifier() {
  const [file, setFile] = useState(null);
  const [result, setResult] = useState(null);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const verifyCertificate = async () => {
    if (!file) {
      alert('请选择证书文件');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await fetch('/api/certificates/verify', {
        method: 'POST',
        body: formData
      });

      const data = await response.json();
      setResult(data.data);
    } catch (error) {
      console.error('验证失败', error);
      alert('验证失败：' + error.message);
    }
  };

  return (
    <div>
      <input 
        type="file" 
        onChange={handleFileChange} 
        accept=".cer,.crt,.pem" 
      />
      <button onClick={verifyCertificate}>验证证书</button>
      
      {result && (
        <div>
          <h3>验证结果</h3>
          <p>状态：{result.valid ? '✅ 通过' : '❌ 失败'}</p>
          <p>消息：{result.message}</p>
          <p>序列号：{result.serialNumber}</p>
          <p>持有者：{result.subject}</p>
          <p>颁发者：{result.issuer}</p>
          <p>有效期：{result.notBefore} 至 {result.notAfter}</p>
          <p>状态：{result.status}</p>
        </div>
      )}
    </div>
  );
}
```

---

## 📚 相关文档

- [P1 功能实现说明](./P1 功能实现说明.md)
- [CA 密钥自动生成修复说明](./CA 密钥自动生成修复说明.md)
- [CA 系统分析与功能建议报告](./CA 系统分析与功能建议报告.md)

---

## 🔗 参考资料

### 数字签名原理
- [RSA 数字签名](https://en.wikipedia.org/wiki/RSA_(cryptosystem))
- [X.509 证书格式](https://en.wikipedia.org/wiki/X.509)

### OpenSSL 命令
- [OpenSSL 验证证书](https://www.openssl.org/docs/man1.1.1/man1/openssl-verify.html)

### 标准规范
- [RFC 5280 - X.509 证书和 CRL](https://tools.ietf.org/html/rfc5280)

---

**创建时间：** 2026-04-13  
**创建人员：** AI Assistant  
**状态：** ✅ 已完成
