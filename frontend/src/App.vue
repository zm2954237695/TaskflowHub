<script setup>
import {computed, onMounted, onUnmounted, ref} from 'vue'
import Home from './views/Home.vue'
import Login from './views/Login.vue'

const token = ref(localStorage.getItem('taskflow_access_token'))
const user = ref(JSON.parse(localStorage.getItem('taskflow_user') || 'null'))
const loggedIn = computed(() => Boolean(token.value))

function onLogin(payload) {
  token.value = payload.accessToken;
  user.value = payload.user
}

function logout() {
  localStorage.removeItem('taskflow_access_token');
  localStorage.removeItem('taskflow_user');
  token.value = '';
  user.value = null
}

onMounted(() => window.addEventListener('taskflow:logout', logout))
onUnmounted(() => window.removeEventListener('taskflow:logout', logout))
</script>
<template>
  <Login v-if="!loggedIn" @success="onLogin"/>
  <Home v-else :user="user" @logout="logout"/>
</template>
