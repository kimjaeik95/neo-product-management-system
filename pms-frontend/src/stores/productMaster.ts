import { productMasterApi } from '@/api/productMasterApi'
import type {
  ProductMaster,
  ProductMasterRequest,
  ProductMasterUpdateRequest,
} from '@/types/productMaster'
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useProductMasterStore = defineStore('productMaster', () => {
  const list = ref<ProductMaster[]>([])
  const categoryItems = ref<ProductMaster[]>([])
  const loading = ref(false)
  const errorMessage = ref('')

  // 페이징 관련
  const page = ref(0)
  const totalPages = ref(0)
  const totalElements = ref(0)

  async function fetchList(targetPage = 0) {
    loading.value = true
    errorMessage.value = ''
    try {
      const result = await productMasterApi.list(targetPage, 20)
      list.value = result.content
      page.value = result.number
      totalPages.value = result.totalPages
      totalElements.value = result.totalElements
    } catch (e) {
      errorMessage.value = '제품마스터 목록을 불러오는데 실패했습니다. 잠시 후 다시 시도해주세요.'
    } finally {
      loading.value = false
    }
  }

  async function fetchProductMaster(productNo: number) {
    return await productMasterApi.get(productNo)
  }

  async function createProductMaster(payload: ProductMasterRequest) {
    await productMasterApi.create(payload)
    await fetchList(0) // 등록 후에 1페이지부터 시작
  }

  async function updateProductMaster(productNo: number, payload: ProductMasterUpdateRequest) {
    await productMasterApi.update(productNo, payload)
    await fetchList(page.value) // 보고 있던 페이지 유지
  }

  async function deleteProductMaster(produtNo: number) {
    await productMasterApi.remove(produtNo)
    await fetchList(page.value)
  }

  async function fetchByCategoryNo(categoryNo: number) {
    loading.value = true
    errorMessage.value = ''

    try {
      const result = await productMasterApi.findByCategoryNo(categoryNo)
      categoryItems.value = result
    } catch (e) {
      errorMessage.value = '제품분류별 제품 조회에 실패했습니다.'
      categoryItems.value = []
    } finally {
      loading.value = false
    }
  }

  return {
    list,
    loading,
    errorMessage,
    page,
    totalPages,
    totalElements,
    fetchList,
    createProductMaster,
    updateProductMaster,
    deleteProductMaster,
    fetchProductMaster,
    categoryItems,
    fetchByCategoryNo,
  }
})
