import { createRouter, createWebHistory } from 'vue-router'
import CategoryListView from '@/views/CategoryListView.vue'
import CategoryFormView from '@/views/CategoryFormView.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/categories' },
    { path: '/categories', name: 'category-list', component: CategoryListView },
    { path: '/categories/new', name: 'category-new', component: CategoryFormView },
    {
      path: '/categories/:categoryNo/edit',
      name: 'category-edit',
      component: CategoryFormView,
      props: true,
    },
  ],
})

export default router
