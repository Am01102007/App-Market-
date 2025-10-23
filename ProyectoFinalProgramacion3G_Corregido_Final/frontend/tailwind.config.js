/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './index.html',
    './src/**/*.{js,jsx,ts,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#10B981', // Green 500
          light: '#6EE7B7', // Green 300
          dark: '#059669',  // Green 600
        },
        secondary: {
          DEFAULT: '#F59E0B', // Amber 500
          light: '#FCD34D', // Amber 300
          dark: '#B45309',  // Amber 700
        },
        accent: {
          DEFAULT: '#8B5CF6', // Violet 500
          light: '#C4B5FD', // Violet 300
          dark: '#6D28D9',  // Violet 700
        },
        neutral: {
          100: '#F3F4F6', // Gray 100
          200: '#E5E7EB', // Gray 200
          300: '#D1D5DB', // Gray 300
          400: '#9CA3AF', // Gray 400
          500: '#6B7280', // Gray 500
          600: '#4B5563', // Gray 600
          700: '#374151', // Gray 700
          800: '#1F2937', // Gray 800
          900: '#111827', // Gray 900
        },
        success: '#10B981',
        warning: '#F59E0B',
        danger: '#EF4444',
      },
      backgroundImage: {
        'gradient-primary': 'linear-gradient(to right, #10B981, #6EE7B7)',
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'Segoe UI', 'Roboto', 'Helvetica', 'Arial', 'sans-serif'],
      },
      boxShadow: {
        soft: '0 4px 12px rgba(0,0,0,0.08)',
        medium: '0 10px 30px rgba(0,0,0,0.12)',
      },
      borderRadius: {
        xl: '1rem',
        '2xl': '1.5rem',
      },
      transitionProperty: {
        'height': 'height',
        'spacing': 'margin, padding',
      }
    },
  },
  plugins: [],
};