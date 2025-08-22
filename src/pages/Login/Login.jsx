import React, { useState, useEffect } from 'react';
import './Login.css';
import { PostAxiosInstance } from '../../axios/AxiosMethod';

const Login = ({ onLogin }) => {
  const [isLoading, setIsLoading] = useState(false);

  const features = [
    { icon: '🤖', text: '챗봇이 추천해주는 메뉴' },
    { icon: '👥', text: '식사 파티원 모집하기' },
    { icon: '🎮', text: '재밌는 내기' },
    { icon: '🗺️', text: '강의장 주변 맛집 찾아보기' }
  ];

  // redirect에서 code 받아서 백엔드로 전달
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const code = params.get('code');
    if (code) {
      setIsLoading(true);
      PostAxiosInstance('/api/oauth/google', { code })
        .then(res => {
          if (res.status >= 200 && res.status < 300) {
            // res.data.user 대신 res.data 전체를 전달하도록 수정
            onLogin(res.data);
          } else {
            alert('로그인 실패');
          }
        })
        .catch(err => {
          console.error(err);
          alert('로그인 중 오류 발생');
        })
        .finally(() => setIsLoading(false));
    }
  }, [onLogin]);

  const handleGoogleLogin = () => {
    const clientId = import.meta.env.VITE_GOOGLE_CLIENT_ID;
    const redirectUri = import.meta.env.VITE_GOOGLE_REDIRECT_URI;
    const scope = 'email profile';
    const authUrl = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=code&scope=${scope}`;
    window.location.href = authUrl;
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <div className="login-header">
          <h1 className="login-title gradient-text">BOBHUB</h1>
          <p className="login-subtitle">
            함께 먹고, 함께 성장하는 개발자들의 공간
          </p>
        </div>

        <div className="login-content">
          <div className="feature-list">
            {features.map((feature, index) => (
              <div key={index} className="feature-item">
                <span className="feature-icon">{feature.icon}</span>
                <span>{feature.text}</span>
              </div>
            ))}
          </div>

          <button
            className="google-login-btn"
            onClick={handleGoogleLogin}
            disabled={isLoading}
          >
            {isLoading ? (
              <>
                <div className="loading-spinner"></div>
                CONNECTING...
              </>
            ) : (
              <>
                <svg className="google-icon" viewBox="0 0 24 24">
                  <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" />
                  <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" />
                  <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z" />
                  <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" />
                </svg>
                CONTINUE WITH GOOGLE
              </>
            )}
          </button>
        </div>
      </div>
    </div>
  );
};

export default Login;
