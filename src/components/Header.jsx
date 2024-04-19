import { Link } from "react-router-dom";
import { LOGIN } from "../constants/page_constant";
import { useState } from "react";
import Logout from "./Logout";

export default function Header() {
    const [isLoggedIn, setIsLoggedIn] = 
        useState(!!localStorage.getItem("access_token"));

    return(
        <header>
            {isLoggedIn ? (
                <Logout>로그아웃</Logout>
            ) : (
                <Link to={LOGIN}>로그인</Link>
            )}
        </header>
    )
}