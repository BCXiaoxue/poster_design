import { defineStore } from "pinia";

const THEME_LIGHT = "light";
const THEME_DARK = "dark";
const THEME_SYSTEM = "system";
const THEME_KEY = "theme";

export const useThemeStore = defineStore("theme", {
  state: () => ({
    themeType: localStorage.getItem(THEME_KEY) || THEME_SYSTEM,
  }),

  actions: {
    setTheme(theme) {
      this.themeType = theme;
      localStorage.setItem(THEME_KEY, theme);

      let targetClass = "";

      if (theme === THEME_LIGHT) {
        targetClass = THEME_LIGHT;
      } else if (theme === THEME_DARK) {
        targetClass = THEME_DARK;
      } else if (theme === THEME_SYSTEM) {
        const prefersDark = window.matchMedia(
          "(prefers-color-scheme: dark)"
        ).matches;
        targetClass = prefersDark ? THEME_DARK : THEME_LIGHT;
      }

      const html = document.documentElement;
      html.classList.remove(THEME_LIGHT, THEME_DARK);
      html.classList.add(targetClass);
    },

    toggleTheme() {
      const nextTheme =
        this.themeType === THEME_DARK ? THEME_LIGHT : THEME_DARK;
      this.setTheme(nextTheme);
    },

    initTheme() {
      this.setTheme(this.themeType);

      if (this.themeType === THEME_SYSTEM) {
        const mediaQuery = window.matchMedia("(prefers-color-scheme: dark)");
        mediaQuery.onchange = () => {
          this.setTheme(THEME_SYSTEM); // 自动切换
        };
      }
    },
  },
});
