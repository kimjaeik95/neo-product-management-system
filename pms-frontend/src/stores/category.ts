import { categoryApi } from '@/api/categoryApi'
import type { Category, CategoryCreateRequest, CategoryUpdatedRequest } from '@/types/category'
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useCategoryStore = defineStore('category', () => {
  // 서버에서 받아온 데이터 저장
  const list = ref<Category[]>([])
  // 화면 로딩
  const loading = ref(false)
  // 프론트 측 오류
  const errorMessage = ref('')

  async function fetchList() {
    loading.value = true
    errorMessage.value = ''
    try {
      list.value = await categoryApi.list()
    } catch (e) {
      errorMessage.value = '제품분류 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요.'
    } finally {
      loading.value = false
    }
  }

  async function fetchCategory(categoryNo: number) {
    return await categoryApi.get(categoryNo)
  }

  async function createCategory(payload: CategoryCreateRequest) {
    await categoryApi.create(payload)
    await fetchList()
  }

  async function updateCategory(categoryNo: number, payload: CategoryUpdatedRequest) {
    await categoryApi.update(categoryNo, payload)
    await fetchList()
  }

  async function deleteCategory(categoryNo: number) {
    await categoryApi.delete(categoryNo)
    await fetchList()
  }

  return {
    list,
    loading,
    errorMessage,
    fetchList,
    fetchCategory,
    createCategory,
    updateCategory,
    deleteCategory,
  }
})
