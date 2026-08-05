import type { PageResponse } from '@/types/common'
import type {
  ProductMaster,
  ProductMasterRequest,
  ProductMasterUpdateRequest,
} from '@/types/productMaster'
import http from './http'

export const productMasterApi = {
  list(page = 0, size = 20): Promise<PageResponse<ProductMaster>> {
    return http.get('/product-masters', { params: { page, size } }).then((res) => res.data)
  },

  get(productNo: number): Promise<ProductMaster> {
    return http.get(`/product-masters/${productNo}`).then((res) => res.data)
  },

  create(payload: ProductMasterRequest): Promise<ProductMaster> {
    return http.post('/product-masters', payload).then((res) => res.data)
  },

  update(productNo: number, payload: ProductMasterUpdateRequest): Promise<ProductMaster> {
    return http.put(`/product-masters/${productNo}`, payload).then((res) => res.data)
  },

  remove(productNo: number): Promise<void> {
    return http.delete(`/product-masters/${productNo}`).then(() => undefined)
  },

  findByCategoryNo(categoryNo: number): Promise<ProductMaster[]> {
    return http.get(`/product-masters/category/${categoryNo}`).then((res) => res.data)
  },
}
