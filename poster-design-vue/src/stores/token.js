import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useTokenStore = defineStore('token', () => {
  // 1.定义数据
  const token = ref(0)

  // 2.定义方法

  // 3.返回数据
  return { token }
},{
  persist: true
})
