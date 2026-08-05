<template>
  <nav v-if="totalPages > 1" class="mt-3">
    <ul class="pagination justify-content-center">
      <li class="page-item" :class="{ disabled: currentPage === 0 }">
        <button class="page-link" @click="$emit('change', currentPage - 1)">이전</button>
      </li>
      <li
        v-for="p in totalPages"
        :key="p"
        class="page-item"
        :class="{ active: p - 1 === currentPage }"
      >
        <button class="page-link" @click="$emit('change', p - 1)">{{ p }}</button>
      </li>
      <li class="page-item" :class="{ disabled: currentPage >= totalPages - 1 }">
        <button class="page-link" @click="$emit('change', currentPage + 1)">다음</button>
      </li>
    </ul>
  </nav>
</template>

<script setup lang="ts">
// currentPage, totalPages는 0부터 시작하는 Spring Data Page 기준입니다.
defineProps<{ currentPage: number; totalPages: number }>()
defineEmits<{ change: [page: number] }>()
</script>
