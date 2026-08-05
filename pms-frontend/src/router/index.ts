import { createRouter, createWebHistory } from 'vue-router'
import CategoryListView from '@/views/CategoryListView.vue'
import CategoryFormView from '@/views/CategoryFormView.vue'
import ProductMasterFormView from '@/views/ProductMasterFormView.vue'
import ProductMasterListView from '@/views/ProductMasterListView.vue'
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
    { path: '/product-masters', name: 'product-master-list', component: ProductMasterListView },
    { path: '/product-masters/new', name: 'product-master-new', component: ProductMasterFormView },

    {
      path: '/product-masters/:productNo/edit',
      name: 'product-master-edit',
      component: ProductMasterFormView,
      props: true,
    },

    {
      path: '/categories/:categoryNo/products',
      name: 'product-masters-by-category',
      component: () => import('@/views/ProductMasterByCategoryView.vue'),
    },
  ],
})

export default router
