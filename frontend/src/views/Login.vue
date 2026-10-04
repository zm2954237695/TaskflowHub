<script setup>
import {ref} from 'vue'
import {api} from '../api/client'

const emit = defineEmits(['success'])
const mode = ref('login'), username = ref('admin@taskflow.local'), password = ref('123456'), displayName = ref(''),
    loading = ref(false), error = ref('')

function switchMode(next) {
  mode.value = next;
  error.value = '';
  if (next === 'register') {
    username.value = '';
    password.value = '';
    displayName.value = ''
  } else {
    username.value = 'admin@taskflow.local';
    password.value = '123456'
  }
}

async function submit() {
  if (!username.value.trim() || !password.value) {
    error.value = '请输入账号和密码';
    return
  }
  if (mode.value === 'register' && password.value.length < 6) {
    error.value = '密码至少需要 6 位';
    return
  }
  loading.value = true;
  error.value = '';
  try {
    const result = mode.value === 'login' ? await api.login({
      username: username.value,
      password: password.value
    }) : await api.register({username: username.value, password: password.value, displayName: displayName.value});
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
      <div class="login-form"><span class="kicker">{{
          mode === 'login' ? 'WELCOME BACK' : 'START WITH TASKFLOW'
        }}</span>
        <h2>{{ mode === 'login' ? '登录工作台' : '创建账号' }}</h2>
        <p class="login-subtitle">{{ mode === 'login' ? '使用你的 TaskFlow 账号继续工作。' : '创建账号后即可开始管理项目。' }}</p>
        <form @submit.prevent="submit"><label>邮箱<input v-model="username" type="email" autocomplete="username"
                                                       placeholder="name@company.com"></label><label
            v-if="mode === 'register'">显示名称<input v-model="displayName" autocomplete="name"
                                                  placeholder="你的称呼"></label><label>密码<input v-model="password"
                                                                                             type="password"
                                                                                             autocomplete="current-password"
                                                                                             placeholder="请输入密码"></label>
          <div v-if="error" class="login-error">{{ error }}</div>
          <button class="btn primary full login-submit" :disabled="loading">
            {{ loading ? '处理中…' : mode === 'login' ? '登录' : '注册并进入' }}
          </button>
        </form>
        <button class="auth-switch" @click="switchMode(mode === 'login' ? 'register' : 'login')">
          {{ mode === 'login' ? '还没有账号？立即注册' : '已有账号？返回登录' }}
        </button>
        <small v-if="mode === 'login'" class="login-hint">演示账号已预填，可直接登录</small></div>
    </section>
  </main>
</template>
