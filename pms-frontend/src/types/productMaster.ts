export interface ProductMaster {
  categoryNo: number
  productNo: number
  productCode: string
  productName: string
  productCreated: string | null
  price: number | null
  used: 'Y' | 'N'
  address: string | null
  createdAt: string
  updatedAt: string | null
  deletedYn: 'Y' | 'N'
  deletedAt: string | null
}

export interface ProductMasterRequest {
  categoryNo: number
  productCode: string
  productName: string
  productCreated?: string | null
  price?: number | null
  address?: string | null
}

export interface ProductMasterUpdateRequest {
  categoryNo: number
  productCode: string
  productName: string
  used: 'Y' | 'N'
  productCreated?: string | null
  price?: number | null
  address?: string | null
}
