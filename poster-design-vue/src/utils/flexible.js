import { computed } from "vue";

/**
 * 判断当前是否为移动设备
 */
// export const isMobile = computed(() => {
//   return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(
//     navigator.userAgent
//   );
// });

import { useWindowSize } from "@vueuse/core";

const { width } = useWindowSize();

export const isMobile = computed(() => width.value < 768);
