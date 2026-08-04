<template>
  <div>
    <h2 class="h4 mb-3">{{ isEdit ? '제품분류 수정' : '제품분류 등록' }}</h2>

    <form @submit.prevent="onSubmit">
      <div class="mb-3">
        <label class="form-label">분류코드</label>
        <input v-model="form.categoryCode" class="form-control" required />
      </div>
      <div class="mb-3">
        <label class="form-label">분류명</label>
        <input v-model="form.categoryName" class="form-control" required />
      </div>
      <div class="mb-3" v-if="isEdit">
        <label class="form-label">사용여부</label>
        <select v-model="form.used" class="form-select">
          <option value="Y">사용</option>
          <option value="N">미사용</option>
        </select>
      </div>

      <button type="submit" class="btn btn-primary">저장</button>
      <router-link to="/categories">
        <button type="button" class="btn btn-outline-secondary ms-2">목록으로</button>
      </router-link>
    </form>
  </div>
</template>

<script setup lang="ts">
import { reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useCategoryStore } from '@/stores/category'
import { categoryApi } from '@/api/categoryApi'

const route = useRoute()
const router = useRouter()
const store = useCategoryStore()

// URL에 categoryNo가 있으면 수정 모드, 없으면 등록 모드
const categoryNo = computed(() =>
  route.params.categoryNo ? Number(route.params.categoryNo) : null,
)
const isEdit = computed(() => categoryNo.value !== null)

const form = reactive({
  categoryCode: '',
  categoryName: '',
  used: 'Y' as 'Y' | 'N',
})

onMounted(async () => {
  if (isEdit.value && categoryNo.value) {
    const data = await categoryApi.get(categoryNo.value)
    form.categoryCode = data.categoryCode
    form.categoryName = data.categoryName
    form.used = data.used
  }
})

async function onSubmit() {
  try {
    if (isEdit.value && categoryNo.value) {
      await store.updateCategory(categoryNo.value, {
        categoryCode: form.categoryCode,
        categoryName: form.categoryName,
        used: form.used,
      })
    } else {
      await store.createCategory({
        categoryCode: form.categoryCode,
        categoryName: form.categoryName,
      })
    }

    router.push('/categories')
  } catch (e: any) {
    alert(e.response?.data?.message ?? '처리 중 오류가 발생했습니다.')
  }
}
</script>
