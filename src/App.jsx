import { Routes, Route } from "react-router-dom";
import {
  MAIN,
  ADMIN_MAIN,
  USER_MAIN,
  SIGN_UP,
  LOGIN,
  PAGE_403,
  PAGE_404
} from "./constants/page_constant";
import MainPage from "./pages/MainPage";
import UserPage from "./pages/UserPage";
import AdminPage from "./pages/AdminPage";
import SignUpPage from "./pages/SignUpPage";
import LoginPage from "./pages/LoginPage";
import Page403 from "./errorPages/Page403";
import Page404 from "./errorPages/Page404";

function App() {
  return (
    <>
      <Routes>
        <Route path={MAIN} element={<MainPage/>} />
        <Route path={USER_MAIN} element={<UserPage/>} />
        <Route path={ADMIN_MAIN} element={<AdminPage/>} />
        <Route path={SIGN_UP} element={<SignUpPage/>} />
        <Route path={LOGIN} element={<LoginPage/>} />
        <Route path={PAGE_403} element={<Page403/>}></Route>
        <Route path={PAGE_404} element={<Page404/>}></Route>
      </Routes>
    </>
  );
}

export default App;
