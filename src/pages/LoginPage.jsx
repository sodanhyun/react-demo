import { useState } from "react"
import { TextField } from "../components/TextField";
import { Link, useNavigate } from "react-router-dom";
import { MAIN, SIGN_UP } from "../constants/page_constant";
import axios from "axios";
import { LOGIN_API } from "../constants/api_constant";
import Header from "../components/Header";

export default function LoginPage() {
    const [id, setId] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        try{
            const formData = new FormData();
            formData.append("id", id);
            formData.append("password", password);

            const response = await axios.post(
                API_BASE_URL + LOGIN_API,
                formData
            )

            localStorage.setItem("access_token", response.data.accessToken);
            localStorage.setItem("refresh_token", response.data.refreshToken);
            localStorage.setItem("authority", response.data.authority);

            navigate(MAIN);
        }catch(error) {
            console.error(error);
            setError("아이디 또는 비밀번호가 잘못되었습니다");
        }
    }

    return(
        <>
        <Header/>
        <form onSubmit={handleLogin}>
            <TextField
                label="아이디"
                type="text"
                name="id"
                required
                value={id}
                onChange={(e) => setId(e.target.value)}
            />
            <TextField
                label="비밀번호"
                type="password"
                name="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            {error && <p style={{color:'red'}}>{error}</p>}
            <button type="submit">로그인</button>
            <Link to={SIGN_UP}>회원가입 하러가기</Link>
        </form>
        </>
    )
}