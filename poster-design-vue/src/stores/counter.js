import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useCounterStore = defineStore('counter', () => {
  // 1.定义数据
  const count = ref(0)
  const doubleCount = computed(() => count.value * 2)
  // 2.定义方法
  function increment() {
    count.value++
  }

  // 3.返回数据
  return { count, doubleCount, increment }
})
//
