/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{js,jsx,ts,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        neon: '#E6FF00', // fondo acento
        navy: '#161A23', // panel oscuro
        slate: '#E9F1FF', // inputs claros
        charcoal: '#535862', // iconos/grises
        accent: '#D9FF00',
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'Segoe UI', 'Roboto', 'Helvetica', 'Arial', 'sans-serif'],
      },
      boxShadow: {
        soft: '0 10px 30px rgba(0,0,0,0.15)',
      },
      borderRadius: {
        xl: '16px',
      },
    },
  },
  plugins: [],
};