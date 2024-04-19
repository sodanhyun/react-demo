import { useEffect, useState } from "react"
import {
    MAIN_INIT_DATA_API
} from "../constants/api_constant"
import fetcher from "../fetcher";
import { ADMIN_MAIN, USER_MAIN } from "../constants/page_constant";
import { Link } from "react-router-dom";
import Header from "../components/Header";

export default function MainPage() {
    const [initData, setInitData] = useState("서버 통신 불가");
    const API_BASE_URL = process.env.REACT_APP_API_BASE_URL;

    const fetchInitData = async () => {
        try{
            const response = await fetcher.get(MAIN_INIT_DATA_API);
            setInitData(response.data);
        }catch(error) {
            console.error("데이터 가져오기 오류", error);
        }
    }

    useEffect(() => {
        fetchInitData();
    }, []);

    return(
        <>
            <Header/>
            <h1>메인 페이지 입니다.</h1>
            <h3>{initData}</h3>
            <div><Link to={USER_MAIN}>유저 페이지로 이동</Link></div>
            <div><Link to={ADMIN_MAIN}>관리자 페이지로 이동</Link></div>
        </>
    )
}