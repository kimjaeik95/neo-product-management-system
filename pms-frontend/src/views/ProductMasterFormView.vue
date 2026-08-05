<template>
  <div>
    <h2 class="h4 mb-3">{{ isEdit ? '제품마스터 수정' : '제품마스터 등록' }}</h2>

    <form @submit.prevent="onSubmit">
      <div class="mb-3">
        <label class="form-label">제품분류</label>
        <select v-model.number="form.categoryNo" class="form-select" required>
          <option disabled value="">분류를 선택하세요</option>
          <option v-for="c in categoryOptions" :key="c.categoryNo" :value="c.categoryNo">
            {{ c.categoryName }}
          </option>
        </select>
      </div>

      <div class="mb-3">
        <label class="form-label">제품코드</label>
        <input v-model="form.productCode" class="form-control" :disabled="isEdit" required />
      </div>

      <div class="mb-3">
        <label class="form-label">제품명</label>
        <input v-model="form.productName" class="form-control" required />
      </div>

      <div class="mb-3">
        <label class="form-label">단가</label>
        <input v-model.number="form.price" type="number" min="0" step="0.01" class="form-control" />
      </div>

      <div class="mb-3">
        <label class="form-label">생산일자</label>
        <input v-model="form.productCreated" type="date" class="form-control" />
      </div>

      <div class="mb-3">
        <label class="form-label">생산지주소</label>
        <input v-model="form.address" class="form-control" />
      </div>

      <!-- 사용여부는 등록(Request)에는 없고 수정(UpdateRequest)에만 있는 필드라, 수정일 때만 보여줍니다. -->
      <div class="mb-3" v-if="isEdit">
        <label class="form-label">사용여부</label>
        <select v-model="form.used" class="form-select">
          <option value="Y">사용</option>
          <option value="N">미사용</option>
        </select>
      </div>

      <button type="submit" class="btn btn-primary">저장</button>
      <router-link to="/product-masters">
        <button type="button" class="btn btn-outline-secondary ms-2">목록으로</button>
      </router-link>
    </form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

import type { Category } from '@/types/category'
import { useProductMasterStore } from '@/stores/productMaster'
import { categoryApi } from '@/api/categoryApi'
import { productMasterApi } from '@/api/productMasterApi'

const route = useRoute()
const router = useRouter()
const store = useProductMasterStore()

const productNo = computed(() => (route.params.productNo ? Number(route.params.productNo) : null))
const isEdit = computed(() => productNo.value !== null)

// 드롭다운용 카테고리 목록. 목록 화면의 페이징(store.list)과는 별개로,
// "전체 분류"가 다 보여야 하므로 넉넉한 size로 직접 조회합니다.
const categoryOptions = ref<Category[]>([])

const form = reactive({
  categoryNo: null as number | null,
  productCode: '',
  productName: '',
  price: null as number | null,
  productCreated: '' as string, // <input type="date">는 'YYYY-MM-DD' 문자열을 그대로 씁니다.
  address: '',
  used: 'Y' as 'Y' | 'N',
})

onMounted(async () => {
  // 카테고리가 100개를 넘어갈 정도로 많아지면 size를 더 늘리거나,
  // 검색 가능한 콤보박스로 바꾸는 걸 고려해보세요.
  const categoryPage = await categoryApi.list(0, 100)
  categoryOptions.value = categoryPage.content

  if (isEdit.value && productNo.value) {
    const data = await productMasterApi.get(productNo.value)
    form.categoryNo = data.categoryNo
    form.productCode = data.productCode
    form.productName = data.productName
    form.price = data.price
    form.productCreated = data.productCreated ?? ''
    form.address = data.address ?? ''
    form.used = data.used
  }
})

async function onSubmit() {
  console.log('현재 form.categoryNo =', form.categoryNo)
  if (form.categoryNo === null) return

  if (isEdit.value && productNo.value) {
    await store.updateProductMaster(productNo.value, {
      categoryNo: form.categoryNo,
      productCode: form.productCode,
      productName: form.productName,
      used: form.used,
      productCreated: form.productCreated || null,
      price: form.price,
      address: form.address || null,
    })
  } else {
    await store.createProductMaster({
      categoryNo: form.categoryNo,
      productCode: form.productCode,
      productName: form.productName,
      productCreated: form.productCreated || null,
      price: form.price,
      address: form.address || null,
    })
  }
  router.push('/product-masters')
}
</script>
