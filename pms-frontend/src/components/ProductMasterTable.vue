<template>
  <div class="table-responsive">
    <table class="table table-bordered table-hover bg-white align-middle">
      <thead class="table-light">
        <tr>
          <!-- 제품분류명 (헤더 텍스트는 기존 요청대로 유지) -->
          <th style="min-width: 120px">제품분류명</th>
          <th style="min-width: 100px">제품코드</th>
          <th style="min-width: 150px">제품명</th>
          <th style="min-width: 100px">단가</th>
          <th style="min-width: 110px">생산일자</th>
          <th style="min-width: 90px">사용여부</th>
          <th style="min-width: 150px">생산지주소</th>
          <th style="min-width: 150px">등록일자</th>
          <th style="min-width: 150px">수정일자</th>
          <!-- 관리 (제품분류와 동일하게 별도 클래스 없이 th 하나로 통일) -->
          <th style="width: 130px" class="text-center">관리</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in items" :key="item.productNo">
          <!-- 1. 제품분류명 데이터 -->
          <td>{{ item.categoryName }}</td>

          <!-- 2. 제품코드 데이터  -->
          <td>{{ item.productCode }}</td>

          <!-- 3. 제품명 데이터 -->
          <td>{{ item.productName }}</td>

          <!-- 4. 단가 데이터 -->
          <td class="text-center">{{ formatPrice(item.price) }}</td>

          <!-- 5. 생산일자 데이터 -->
          <td class="text-center">{{ item.productCreated ?? '-' }}</td>

          <!-- 6. 사용여부 뱃지 -->
          <td class="text-center">
            <span
              class="badge"
              :class="item.used === 'Y' ? 'text-bg-success' : 'text-bg-secondary'"
            >
              {{ item.used === 'Y' ? '사용' : '미사용' }}
            </span>
          </td>

          <!-- 7. 생산지주소 데이터 -->
          <td class="text-center">{{ item.address ?? '-' }}</td>

          <!-- 8. 등록일자 데이터 -->
          <td class="text-center">{{ formatDate(item.createdAt) }}</td>

          <!-- 9. 수정일자 데이터 -->
          <td class="text-center">{{ formatDate(item.updatedAt) }}</td>

          <!-- 10. 관리 버튼 영역  -->
          <td class="text-center align-middle text-nowrap">
            <div class="d-inline-flex gap-1">
              <router-link
                :to="{
                  path: `/product-masters/${item.productNo}/edit`,
                  query: { redirect: route.fullPath },
                }"
              >
                <button class="btn btn-outline-secondary btn-sm">수정</button>
              </router-link>
              <button class="btn btn-outline-danger btn-sm" @click="emit('delete', item.productNo)">
                삭제
              </button>
            </div>
          </td>
        </tr>
        <tr v-if="items.length === 0">
          <td colspan="10" class="text-center text-muted py-4">등록된 제품이 없습니다.</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import type { ProductMaster } from '@/types/productMaster'
import { useRoute } from 'vue-router'
defineProps<{ items: ProductMaster[] }>()
const route = useRoute()
const emit = defineEmits<{ delete: [productNo: number] }>()

function formatPrice(price: number | null) {
  if (price === null || price === undefined) return '-'
  return price.toLocaleString('ko-KR') + '원'
}

function formatDate(value: string | null) {
  if (!value) return '-'
  return value.replace('T', ' ').substring(0, 16)
}
</script>
