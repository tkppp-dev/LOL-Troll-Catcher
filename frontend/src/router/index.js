import { createRouter, createWebHistory } from "vue-router";
import Home from '../views/Home'
import SingleSearchResult from '../views/SingleSearchResult'

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/search/single",
    name: "SingleSearchResult",
    component: SingleSearchResult,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
