import type { Category, CategoryCreateRequest, CategoryUpdatedRequest } from '@/types/category'
import http from './http'
import type { PageResponse } from '@/types/common'
export const categoryApi = {
  // GET /categories?page=0&size=20  (Pageable)
  list(page = 0, size = 20): Promise<PageResponse<Category>> {
    return http.get('/categories', { params: { page, size } }).then((res) => res.data)
  },
  get(categoryNo: number): Promise<Category> {
    return http.get(`/categories/${categoryNo}`).then((res) => res.data)
  },

  create(payload: CategoryCreateRequest): Promise<Category> {
    return http.post('/categories', payload).then((res) => res.data)
  },

  update(categoryNo: number, payload: CategoryUpdatedRequest): Promise<Category> {
    return http.put(`/categories/${categoryNo}`, payload).then((res) => res.data)
  },

  delete(categoryNo: number): Promise<void> {
    return http.delete(`/categories/${categoryNo}`).then(() => undefined)
  },
}
