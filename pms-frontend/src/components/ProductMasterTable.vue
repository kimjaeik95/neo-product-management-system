<template>
  <table class="table table-bordered table-hover bg-white align-middle">
    <thead class="table-light">
      <tr>
        <th>제품분류명</th>
        <th>제품코드</th>
        <th>제품명</th>
        <th>단가</th>
        <th>생산일자</th>
        <th>사용여부</th>
        <th>생산지주소</th>
        <th>등록일자</th>
        <th>수정일자</th>
        <th style="width: 180px">관리</th>
      </tr>
    </thead>

    <tr v-for="item in items" :key="item.productNo">
      <td>
        <router-link :to="`/product-masters/${item.productNo}`">{{ item.productCode }}</router-link>
      </td>
      <td>{{ item.categoryNo }}</td>
      <td>{{ item.productCode }}</td>
      <td>{{ item.productName }}</td>
      <td>{{ formatPrice(item.price) }}</td>
      <td>{{ item.productCreated ?? '-' }}</td>
      <td>{{ item.address }}</td>
      <td>{{ item.createdAt }}</td>
      <td>{{ item.updatedAt }}</td>
      <td>
        <span class="badge" :class="item.used === 'Y' ? 'text-bg-success' : 'text-bg-secondary'">
          {{ item.used === 'Y' ? '사용' : '미사용' }}
        </span>
      </td>
      <td>
        <router-link :to="`/product-masters/${item.productNo}/edit`">
          <button class="btn btn-outline-secondary btn-sm">수정</button>
        </router-link>
        <button class="btn btn-outline-danger btn-sm ms-1" @click="emit('delete', item.categoryNo)">
          삭제
        </button>
      </td>
    </tr>
    <tr v-if="items.length === 0">
      <td colspan="6" class="text-center text-muted py-4">등록된 제품이 없습니다.</td>
    </tr>
  </table>
</template>

<script setup lang="ts">
import type { ProductMaster } from '@/types/productMaster'

defineProps<{ items: ProductMaster[] }>()

const emit = defineEmits<{ delete: [productNo: number] }>()

function formatPrice(price: number | null) {
  if (price === null || price === undefined) return '-'
  return price.toLocaleString('ko-KR') + '원'
}
</script>
