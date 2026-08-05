<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h2 class="h4 m-0">제품분류 목록</h2>

      <div class="d-flex gap-2">
        <router-link to="/product-masters">
          <button class="btn btn-outline-secondary btn-sm">제품마스터 목록</button>
        </router-link>

        <router-link to="/categories/new">
          <button class="btn btn-primary btn-sm">+ 신규 등록</button>
        </router-link>
      </div>
    </div>

    <div v-if="store.loading" class="text-muted">불러오는 중...</div>
    <div v-else-if="store.errorMessage" class="alert alert-danger">{{ store.errorMessage }}</div>
    <CategoryTable v-else :items="store.list" @delete="onDeleteCategory" />
    <Pagination :current-page="store.page" :total-pages="store.totalPages" @change="onPageChange" />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useCategoryStore } from '@/stores/category'
import CategoryTable from '@/components/CategoryTable.vue'
import Pagination from '@/components/Pagination.vue'

// 이 화면은 "store에서 데이터 가져와서 표에 넘겨주기"만 합니다.
const store = useCategoryStore()

onMounted(() => {
  store.fetchList(0)
})

function onPageChange(page: number) {
  store.fetchList(page)
}

async function onDeleteCategory(categoryNo: number) {
  if (!confirm('삭제하시겠습니까?')) return

  try {
    await store.deleteCategory(categoryNo)

    alert('삭제되었습니다.')
    await store.fetchList()
  } catch (e: any) {
    alert(e.response?.data?.message ?? '삭제 중 오류가 발생했습니다.')
  }
}
</script>
