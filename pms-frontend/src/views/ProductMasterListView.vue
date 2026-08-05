<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h2 class="h4 m-0">제품마스터 목록</h2>

      <div class="d-flex gap-2">
        <!-- 제품분류 화면으로 이동하는 버튼 추가 -->
        <router-link to="/categories">
          <button class="btn btn-outline-secondary btn-sm">제품분류 목록</button>
        </router-link>

        <router-link to="/product-masters/new">
          <button class="btn btn-primary btn-sm">+ 신규 등록</button>
        </router-link>
      </div>
    </div>
    <div v-if="store.loading" class="text-muted">불러오는 중...</div>
    <div v-else-if="store.errorMessage" class="alert alert-danger">{{ store.errorMessage }}</div>
    <template v-else>
      <ProductMasterTable :items="store.list" @delete="onDelete" />
      <Pagination
        :current-page="store.page"
        :total-pages="store.totalPages"
        @change="onPageChange"
      />
    </template>
  </div>
</template>

<script setup lang="ts">
import { useProductMasterStore } from '@/stores/productMaster'
import { onMounted } from 'vue'
import Pagination from '@/components/Pagination.vue'
import ProductMasterTable from '@/components/productMasterTable.vue'

const store = useProductMasterStore()

onMounted(() => {
  store.fetchList(0)
})

function onPageChange(page: number) {
  store.fetchList(page)
}

async function onDelete(productNo: number) {
  if (!confirm('삭제하시겠습니까?')) return

  try {
    await store.deleteProductMaster(productNo)

    alert('삭제되었습니다.')
    await store.fetchList()
  } catch (e: any) {
    alert(e.response?.data?.message ?? '삭제 중 오류가 발생했습니다.')
  }
}
</script>
