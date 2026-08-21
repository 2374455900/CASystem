<template>
  <div id="app">
    <router-view v-slot="{ Component, route }">
      <transition name="page-fade" mode="out-in">
        <component :is="Component" :key="route.path" />
      </transition>
    </router-view>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
@import './styles/design-system.css';
@import './assets/styles/main.css';

.page-fade-enter-active,
.page-fade-leave-active {
  transition: all 0.35s cubic-bezier(0.25, 0.1, 0.25, 1);
}

.page-fade-enter-from,
.page-fade-leave-to {
  opacity: 0;
  transform: translateY(-12px) scale(0.97);
}

@media (prefers-reduced-motion: reduce) {
  .page-fade-enter-active,
  .page-fade-leave-active {
    transition: none;
  }
  .page-fade-enter-from,
  .page-fade-leave-to {
    opacity: 0;
    transform: none;
  }
}
</style>
