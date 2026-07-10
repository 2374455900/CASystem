# CA 密钥自动生成修复说明

**修复日期：** 2026-04-13  
**问题：** 证书生成异常：`C:\keys\PrivateKey.pem` 文件不存在导致申请证书报错

---

## 🐛 问题原因

**KeyManager.loadKeys() 方法在密钥文件不存在时直接抛出异常，没有自动生成密钥对。**

错误日志：
```
证书生成异常：C:\keys\PrivateKey.pem
java.nio.file.NoSuchFileException: C:\keys\PrivateKey.pem
```

### 原有逻辑

```java
public void loadKeys(String keyDirectory) throws Exception {
    String privateKeyPath = keyDirectory + "/PrivateKey.pem";
    String publicKeyPath = keyDirectory + "/PublicKey.pem";

    // 直接读取文件，如果不存在会抛出异常
    String privateKeyContent = new String(java.nio.file.Files.readAllBytes(
            java.nio.file.Paths.get(privateKeyPath)));
    // ...
}
```

---

## ✅ 修复方案

### 修复内容

**文件：** `backend/src/main/java/com/kuugae/ca/utils/KeyManager.java`

**新增功能：**
1. ✅ 检查密钥文件是否存在
2. ✅ 如果不存在，自动生成新的 RSA 密钥对
3. ✅ 将生成的密钥保存为 PEM 格式
4. ✅ 自动创建目录结构
5. ✅ 添加详细日志记录

### 核心代码

```java
public void loadKeys(String keyDirectory) throws Exception {
    String privateKeyPath = keyDirectory + "/PrivateKey.pem";
    String publicKeyPath = keyDirectory + "/PublicKey.pem";

    File privateKeyFile = new File(privateKeyPath);
    File publicKeyFile = new File(publicKeyPath);

    // 如果密钥文件不存在，则生成新的密钥对
    if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
        log.info("CA 密钥文件不存在，将生成新的密钥对");
        generateAndSaveKeyPair(keyDirectory);
    }

    // 读取已存在的密钥文件
    String privateKeyContent = new String(java.nio.file.Files.readAllBytes(
            java.nio.file.Paths.get(privateKeyPath)));
    String publicKeyContent = new String(java.nio.file.Files.readAllBytes(
            java.nio.file.Paths.get(publicKeyPath)));

    this.privateKey = loadPrivateKey(privateKeyContent);
    this.publicKey = loadPublicKey(publicKeyContent);
    
    log.info("CA 密钥加载成功");
}
```

### 新增方法

#### 1. generateAndSaveKeyPair - 生成并保存密钥对

```java
private void generateAndSaveKeyPair(String keyDirectory) throws Exception {
    // 创建目录
    File dir = new File(keyDirectory);
    if (!dir.exists()) {
        boolean created = dir.mkdirs();
        if (created) {
            log.info("创建密钥目录：{}", keyDirectory);
        }
    }

    // 生成密钥对（RSA 2048 位）
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    
    this.privateKey = keyPair.getPrivate();
    this.publicKey = keyPair.getPublic();

    // 保存私钥
    String privateKeyPem = encodePrivateKeyToPem(this.privateKey);
    String privateKeyPath = keyDirectory + "/PrivateKey.pem";
    saveToFile(privateKeyPath, privateKeyPem);
    log.info("CA 私钥已保存至：{}", privateKeyPath);

    // 保存公钥
    String publicKeyPem = encodePublicKeyToPem(this.publicKey);
    String publicKeyPath = keyDirectory + "/PublicKey.pem";
    saveToFile(publicKeyPath, publicKeyPem);
    log.info("CA 公钥已保存至：{}", publicKeyPath);
}
```

#### 2. encodePrivateKeyToPem - 私钥 PEM 编码

```java
private String encodePrivateKeyToPem(PrivateKey privateKey) {
    byte[] encoded = privateKey.getEncoded();
    String base64 = Base64.getEncoder().encodeToString(encoded);
    
    StringBuilder pem = new StringBuilder();
    pem.append("-----BEGIN PRIVATE KEY-----\n");
    // 每行 64 个字符
    for (int i = 0; i < base64.length(); i += 64) {
        int end = Math.min(i + 64, base64.length());
        pem.append(base64.substring(i, end)).append("\n");
    }
    pem.append("-----END PRIVATE KEY-----\n");
    return pem.toString();
}
```

#### 3. encodePublicKeyToPem - 公钥 PEM 编码

```java
private String encodePublicKeyToPem(PublicKey publicKey) {
    byte[] encoded = publicKey.getEncoded();
    String base64 = Base64.getEncoder().encodeToString(encoded);
    
    StringBuilder pem = new StringBuilder();
    pem.append("-----BEGIN PUBLIC KEY-----\n");
    // 每行 64 个字符
    for (int i = 0; i < base64.length(); i += 64) {
        int end = Math.min(i + 64, base64.length());
        pem.append(base64.substring(i, end)).append("\n");
    }
    pem.append("-----END PUBLIC KEY-----\n");
    return pem.toString();
}
```

#### 4. saveToFile - 保存文件

```java
private void saveToFile(String filePath, String content) throws IOException {
    File file = new File(filePath);
    File parent = file.getParentFile();
    if (parent != null && !parent.exists()) {
        parent.mkdirs();
    }
    
    try (FileOutputStream fos = new FileOutputStream(file)) {
        fos.write(content.getBytes("UTF-8"));
    }
}
```

---

## 🔄 工作流程

### 首次启动流程

```
1. 应用启动
   ↓
2. CertificateService 初始化
   ↓
3. 调用 KeyManager.loadKeys("C:/keys")
   ↓
4. 检查 C:/keys/PrivateKey.pem 是否存在
   ↓
5. 文件不存在 → 调用 generateAndSaveKeyPair()
   ↓
6. 创建目录 C:/keys
   ↓
7. 生成 RSA 2048 密钥对
   ↓
8. 保存私钥到 C:/keys/PrivateKey.pem
   ↓
9. 保存公钥到 C:/keys/PublicKey.pem
   ↓
10. 加载密钥到内存
   ↓
11. 初始化完成
```

### 后续启动流程

```
1. 应用启动
   ↓
2. CertificateService 初始化
   ↓
3. 调用 KeyManager.loadKeys("C:/keys")
   ↓
4. 检查密钥文件存在
   ↓
5. 从文件读取密钥
   ↓
6. 加载密钥到内存
   ↓
7. 初始化完成
```

---

## 📁 生成的文件

### 1. 私钥文件：`C:/keys/PrivateKey.pem`

```
-----BEGIN PRIVATE KEY-----
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC7VJTUt9Us8cKj
MzEfYyjiWA4R4/M2bS1+fWIcPm15j5sHkUy5vHv7K1rX4q7s5vP7qR5q5s5vP7qR
...（中间省略）...
x5s5vP7qR5q5s5vP7qR5q5s5vP7qR5q5s5vP7qR5q5s5vP7qR5q5s5vP7qR5q5s5
-----END PRIVATE KEY-----
```

### 2. 公钥文件：`C:/keys/PublicKey.pem`

```
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu1SU1LfVLPHCozMxH2Mp
...（中间省略）...
H2MpH2MpH2MpH2MpH2MpH2MpH2MpH2MpH2MpH2MpH2MpH2MpH2MpH2MpH2MpH2Mp
-----END PUBLIC KEY-----
```

---

## 🧪 测试验证

### 测试步骤

1. **删除现有密钥（模拟首次启动）**
```bash
# Windows
rmdir /s /q C:\keys

# Linux/Mac
rm -rf /keys
```

2. **启动应用**
```bash
cd backend
mvn spring-boot:run
```

3. **查看日志**

**预期日志：**
```
INFO  KeyManager - CA 密钥文件不存在，将生成新的密钥对
INFO  KeyManager - 创建密钥目录：C:/keys
INFO  KeyManager - CA 私钥已保存至：C:/keys/PrivateKey.pem
INFO  KeyManager - CA 公钥已保存至：C:/keys/PublicKey.pem
INFO  KeyManager - CA 密钥加载成功
```

4. **检查文件**

确认文件已生成：
```
C:/keys/
├── PrivateKey.pem  (约 1.7 KB)
└── PublicKey.pem   (约 0.5 KB)
```

5. **测试证书申请**

访问 http://localhost:8088/apply，填写表单并提交

**预期结果：**
- ✅ 证书申请成功
- ✅ 证书正常生成和下载

---

## 📊 修复前后对比

| 项目 | 修复前 | 修复后 |
|------|--------|--------|
| 密钥文件不存在 | ❌ 抛出异常 | ✅ 自动生成 |
| 目录不存在 | ❌ 抛出异常 | ✅ 自动创建 |
| 首次启动 | ❌ 需要手动准备密钥 | ✅ 自动初始化 |
| 日志记录 | ❌ 无 | ✅ 详细日志 |
| PEM 格式 | ❌ 仅支持读取 | ✅ 支持读写 |
| 安全性 | ⚠️ 密钥需手动生成 | ✅ 自动生成随机密钥 |

---

## 🔐 安全性说明

### 密钥生成

- **算法：** RSA
- **密钥长度：** 2048 位
- **随机性：** 使用 Java SecureRandom
- **格式：** PEM（Base64 编码）

### 密钥存储

- **位置：** 由配置文件指定（默认：`C:/keys`）
- **权限：** 建议设置为仅应用可读
- **备份：** 建议定期备份密钥文件

### 注意事项

⚠️ **重要：**
1. 私钥文件包含敏感信息，请妥善保管
2. 生产环境建议使用 HSM（硬件安全模块）
3. 定期轮换密钥（建议每年一次）
4. 限制密钥文件的访问权限

---

## 🛠️ 配置说明

### 修改密钥目录位置

在 `application.yml` 中配置：

```yaml
app:
  certificate:
    key-directory: D:/secure/ca/keys  # 自定义密钥目录
```

或使用环境变量：

```bash
export KEY_DIRECTORY=D:/secure/ca/keys
```

### 生产环境建议

```yaml
# 生产环境配置示例
app:
  certificate:
    key-directory: /etc/ca/keys  # Linux 路径
    # 或使用环境变量
    # key-directory: ${CA_KEY_DIRECTORY}
```

---

## 📝 常见问题

### Q1: 密钥文件权限问题

**问题：** 应用无法写入密钥文件

**解决：**
```bash
# Windows - 以管理员身份运行
# Linux/Mac - 设置正确权限
chmod 700 /keys
chown ca-user:ca-group /keys
```

### Q2: 密钥丢失怎么办

**问题：** 密钥文件被删除或损坏

**解决：**
1. 删除损坏的密钥文件
2. 重启应用，会自动生成新密钥
3. ⚠️ 注意：旧证书将失效，需要重新签发

### Q3: 如何查看已生成的密钥

**查看公钥信息：**
```bash
openssl rsa -pubin -in PublicKey.pem -text -noout
```

**查看私钥信息（谨慎操作）：**
```bash
openssl rsa -in PrivateKey.pem -text -noout
```

---

## ✅ 修复验证清单

- [x] KeyManager 添加密钥文件检查
- [x] 实现自动生成密钥对功能
- [x] 实现 PEM 格式编码
- [x] 实现文件保存功能
- [x] 添加目录自动创建
- [x] 添加详细日志记录
- [x] 编译成功
- [ ] 启动应用测试（需要用户手动测试）
- [ ] 证书申请测试（需要用户手动测试）

---

## 📚 相关文档

- [P1 功能实现说明](./P1 功能实现说明.md)
- [P1 功能部署说明](./P1 功能部署说明.md)
- [快速启动指南](./快速启动指南.md)
- [CA 系统分析与功能建议报告](./CA 系统分析与功能建议报告.md)

---

**修复完成时间：** 2026-04-13  
**修复人员：** AI Assistant  
**状态：** ✅ 等待用户测试验证
