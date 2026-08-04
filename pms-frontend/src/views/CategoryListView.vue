<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h2 class="h4 m-0">제품분류 목록</h2>
      <router-link to="/categories/new">
        <button class="btn btn-primary btn-sm">+ 신규 등록</button>
      </router-link>
    </div>

    <div v-if="store.loading" class="text-muted">불러오는 중...</div>
    <div v-else-if="store.errorMessage" class="alert alert-danger">{{ store.errorMessage }}</div>
    <CategoryTable v-else :items="store.list" @delete="onDelete" />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useCategoryStore } from '@/stores/category'
import CategoryTable from '@/components/CategoryTable.vue'

// 이 화면은 "store에서 데이터 가져와서 표에 넘겨주기"만 합니다.
const store = useCategoryStore()

onMounted(() => {
  store.fetchList()
})

async function onDelete(categoryNo: number) {
  await store.deleteCategory(categoryNo)
}
</script>
