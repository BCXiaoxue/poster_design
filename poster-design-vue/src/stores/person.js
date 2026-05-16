import { ref, computed } from "vue";
import { defineStore } from "pinia";

export const usePersonStore = defineStore(
  "person",
  () => {
    const imgUrl = ref("");
    const userId = ref(0);
    const authority = ref(2);

    const userInfo = computed(() => ({
      id: userId.value,
      avatar: imgUrl.value,
      authority: authority.value,
    }));

    function resetUser() {
      userId.value = 0;
      imgUrl.value = "";
      authority.value = 2;
    }

    return { imgUrl, userId, userInfo, authority, resetUser };
  },
  {
    persist: true,
  }
);
