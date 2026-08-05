<template>
  <table class="table table-bordered table-hover bg-white align-middle">
    <thead class="table-light">
      <tr>
        <th>제품분류코드</th>
        <th>제품분류명</th>
        <th>사용여부</th>
        <th>등록일자</th>
        <th>수정일자</th>
        <th style="width: 90px"></th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in items" :key="item.categoryNo">
        <td>{{ item.categoryCode }}</td>
        <td>{{ item.categoryName }}</td>
        <td>
          <span class="badge" :class="item.used === 'Y' ? 'text-bg-success' : 'text-bg-secondary'">
            {{ item.used === 'Y' ? '사용' : '미사용' }}
          </span>
        </td>
        <td>{{ formatDate(item.createdAt) }}</td>
        <td :class="{ 'text-center': !item.updatedAt }">
          {{ formatDate(item.updatedAt) }}
        </td>
        <td class="text-center align-middle text-nowrap">
          <div class="d-inline-flex gap-1">
            <router-link :to="`/categories/${item.categoryNo}/products`">
              <button class="btn btn-outline-primary btn-sm">제품 보기</button>
            </router-link>

            <router-link :to="`/categories/${item.categoryNo}/edit`">
              <button class="btn btn-outline-secondary btn-sm">수정</button>
            </router-link>
            <button class="btn btn-outline-danger btn-sm" @click="emit('delete', item.categoryNo)">
              삭제
            </button>
          </div>
        </td>
      </tr>
      <tr v-if="items.length === 0">
        <td colspan="6" class="text-center text-muted py-4">등록된 제품분류가 없습니다.</td>
      </tr>
    </tbody>
  </table>
</template>

<script setup lang="ts">
import type { Category } from '@/types/category'

// 부모 컴포넌트에서 전달한 items 목록을 props로 받습니다.
defineProps<{ items: Category[] }>()

const emit = defineEmits<{ delete: [categoryNo: number] }>()

function formatDate(value: string | null | undefined) {
  if (!value) return '-' // 값이 없으면 '-' 리턴
  return value.replace('T', ' ').substring(0, 16)
}
</script>
