<script setup>
import {ref} from 'vue'
import {api} from '../api/client'

const emit = defineEmits(['success'])
const username = ref('admin@taskflow.local'), password = ref('123456'), loading = ref(false), error = ref('')

async function submit() {
  if (!username.value.trim() || !password.value) {
    error.value = '请输入账号和密码';
    return
  }
  loading.value = true;
  error.value = '';
  try {

    try {
      const result = await api.login({
        username: username.value,
        password: password.value
      })
      console.log('登录成功：', result)
    } catch (error) {
      console.error('登录失败：', error)
    }
    const result = await api.login({username: username.value, password: password.value});

    localStorage.setItem('taskflow_access_token', result.accessToken);
    localStorage.setItem('taskflow_user', JSON.stringify(result.user));
    emit('success', result)
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}
</script>
<template>
  <main class="login-page">
    <section class="login-visual">
      <div class="login-mark">TF</div>
      <div class="login-visual-copy"><span class="kicker">TASKFLOW HUB / WORKSPACE</span>
        <h1>把复杂的协作，<br><em>变成清晰的下一步。</em></h1>
        <p>集中管理项目、任务和团队进度，让每一次推进都留下痕迹。</p></div>
      <div class="login-orbit orbit-a"></div>
      <div class="login-orbit orbit-b"></div>
      <small class="login-footer">A focused workspace for teams that ship.</small></section>
    <section class="login-panel">
      <div class="login-form"><span class="kicker">WELCOME BACK</span>
        <h2>登录工作台</h2>
        <p class="login-subtitle">使用你的 TaskFlow 账号继续工作。</p>
        <form @submit.prevent="submit"><label>账号<input v-model="username" type="email" autocomplete="username"
                                                       placeholder="name@company.com"></label><label>密码<input
            v-model="password" type="password" autocomplete="current-password" placeholder="请输入密码"></label>
          <div v-if="error" class="login-error">{{ error }}</div>
          <button class="btn primary full login-submit" :disabled="loading">{{ loading ? '正在登录…' : '登录' }}</button>
        </form>
        <small class="login-hint">演示账号已预填，可直接登录</small></div>
    </section>
  </main>
</template>
