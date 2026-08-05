<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <div>
        <h2 class="h4 m-0">{{ categoryName }} - 제품 목록</h2>
        <p class="text-muted small mb-0 mt-1">
          이 제품분류에 등록된 제품마스터 목록입니다. (전체 {{ store.categoryItems.length }}건)
        </p>
      </div>

      <router-link to="/categories">
        <button class="btn btn-outline-secondary btn-sm">목록으로</button>
      </router-link>
    </div>

    <div v-if="store.loading" class="text-muted">불러오는 중...</div>
    <div v-else-if="store.errorMessage" class="alert alert-danger">
      {{ store.errorMessage }}
    </div>
    <!-- 카테고리별 조회는 백엔드에서 페이징 없이 List로 내려주므로 Pagination 컴포넌트는 사용하지 않습니다 -->
    <ProductMasterTable v-else :items="store.categoryItems" @delete="onDelete" />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useProductMasterStore } from '@/stores/productMaster'
import ProductMasterTable from '@/components/productMasterTable.vue'

const route = useRoute()
const store = useProductMasterStore()

// 라우터 params는 항상 string 이라 Number로 변환
const categoryNo = computed(() => Number(route.params.categoryNo))

// 목록 첫 번째 항목의 categoryName을 헤더에 재사용 (조회 결과가 있을 때만 표시됨)
const categoryName = computed(() => store.categoryItems[0]?.categoryName ?? '제품분류')

onMounted(() => {
  store.fetchByCategoryNo(categoryNo.value)
})

// 같은 페이지에서 categoryNo가 바뀌는 경우(예: 다른 카테고리 링크로 이동) 재조회
watch(categoryNo, (newCategoryNo) => {
  store.fetchByCategoryNo(newCategoryNo)
})

async function onDelete(productNo: number) {
  if (!confirm('삭제하시겠습니까?')) return

  try {
    await store.deleteProductMaster(productNo)
    alert('삭제되었습니다.')
    await store.fetchByCategoryNo(categoryNo.value)
  } catch (e: any) {
    alert(e.response?.data?.message ?? '삭제 중 오류가 발생했습니다.')
  }
}
</script>
