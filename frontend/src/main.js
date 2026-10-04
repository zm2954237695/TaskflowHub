import {createApp} from 'vue';
import {createPinia} from 'pinia';
import {createRouter, createWebHistory} from 'vue-router';
import App from './App.vue';
import Home from './views/Home.vue';
import Admin from './views/Admin.vue';
import './style.css';

const router = createRouter({
    history: createWebHistory(),
    routes: [{path: '/', component: Home}, {path: '/admin', component: Admin}]
});
createApp(App).use(createPinia()).use(router).mount('#app');
