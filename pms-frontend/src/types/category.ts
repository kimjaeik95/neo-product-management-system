export interface Category {
  categoryNo: number
  categoryCode: string
  categoryName: string
  used: 'Y' | 'N'
  createdAt: string
  updatedAt: string | null
  deletedYn: 'Y' | 'N'
  deletedAt: string | null
}

export interface CategoryCreateRequest {
  categoryCode: string
  categoryName: string
}

export interface CategoryUpdatedRequest {
  categoryCode: string
  categoryName: string
  used: 'Y' | 'N'
}
