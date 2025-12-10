<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI 여행 루트 계획</title>
    
    <%@ include file="/WEB-INF/views/inc/asset.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/asset/css/route.css">
</head>
<body class="ai-plan-page">
    <%@ include file="/WEB-INF/views/inc/header.jsp" %>
    
    <main class="ai-plan-main-container">
        <div class="progress-container">
            <div class="progress-bar" id="progressBar"></div>
        </div>
        
        <div id="ai-plan-container">
            <div id="general-questions">
                <%-- 1. 도시 --%>
                <div class="question-step active" data-question-key="city">
                    <h2>떠나고 싶은 도시🏙️</h2>
                    <div class="card-options grid-3">
                        <div class="card" data-value="서울">서울</div>
                        <div class="card" data-value="제주">제주</div>
                        <div class="card" data-value="부산">부산</div>
                        <div class="card" data-value="강릉">강릉</div>
                        <div class="card" data-value="전주">전주</div>
                        <div class="card" data-value="경주">경주</div>
                        <div class="card" data-value="인천">인천</div>
                        <div class="card" data-value="대구">대구</div>
                        <div class="card" data-value="대전">대전</div>
                        <div class="card" data-value="춘천">춘천</div>
                    </div>
                </div>
                
                <%-- 2. 기간 --%>
                <div class="question-step" data-question-key="duration">
                    <h2>여행기간은🗓️</h2>
                    <div class="card-options grid-3">
                        <div class="card" data-value="0">당일치기</div>
                        <div class="card" data-value="1">1박 2일</div>
                        <div class="card" data-value="2">2박 3일</div>
                        <div class="card" data-value="3">3박 4일</div>
                        <div class="card" data-value="4">4박 5일</div>
                        <div class="card" data-value="5">5박 이상</div>
                    </div>
                </div>
                
                <%-- 3. 날짜 선택 (달력) --%>
                <div class="question-step" id="date-selection-step" data-question-key="dateSelection">
                    <h2>일정 선택 📅</h2>
                    <p class="description" id="date-selection-guide">여행 시작일을 선택해주세요.</p>
                    <div id="calendar-container">
                        <div class="calendar-header">
                            <button id="prev-month">&lt;</button>
                            <span id="current-month-year"></span>
                            <button id="next-month">&gt;</button>
                        </div>
                        <div class="calendar-grid days">
                            <div>일</div><div>월</div><div>화</div><div>수</div><div>목</div><div>금</div><div>토</div>
                        </div>
                        <div class="calendar-grid dates" id="calendar-dates"></div>
                    </div>
                </div>
        
                <%-- 4. 여행 스타일 --%>
                <div class="question-step" data-question-key="travelStyle">
                    <h2>여행 스타일😎</h2>
                    <div class="card-options grid-3">
                        <div class="card" data-value="힐링">힐링</div>
                        <div class="card" data-value="관광">관광</div>
                        <div class="card" data-value="체험">체험</div>
                        <div class="card" data-value="음식">음식</div>
                        <div class="card" data-value="쇼핑">쇼핑</div>
                        <div class="card" data-value="헬스케어">헬스케어</div>
                    </div>
                </div>

                <%-- 5. 활동 시간 --%>
                <div class="question-step" data-question-key="activityTime">
                    <h2>활동 시간 선호🕰️</h2>
                     <div class="card-options grid-2">
                        <div class="card" data-value="아침형">아침형</div>
                        <div class="card" data-value="낮형">낮형</div>
                        <div class="card" data-value="저녁형">저녁형</div>
                        <div class="card" data-value="상관없음">상관없음</div>
                    </div>
                </div>
                
                <%-- 6. 예산 --%>
                <div class="question-step" data-question-key="budget">
                    <h2>예산범위(1인 기준)💰</h2>
                    <div class="card-options grid-2">
                        <div class="card" data-value="10만원 이하">10만원이하</div>
                        <div class="card" data-value="10만원 ~ 30만원">10만원 ~ 30만원</div>
                        <div class="card" data-value="30만원 ~ 50만원">30만원 ~ 50만원</div>
                        <div class="card" data-value="50만원 이상">50만원 이상</div>
                    </div>
                </div>
                
                <%-- 7. 선호 지역 --%>
                <div class="question-step" data-question-key="preferredArea">
                    <h2>선호 지역🌳</h2>
                    <div class="card-options grid-2">
                        <div class="card" data-value="자연">자연</div>
                        <div class="card" data-value="도심">도심</div>
                        <div class="card" data-value="문화유적">문화유적</div>
                        <div class="card" data-value="테마파크">테마파크</div>
                    </div>
                </div>
                
                <%-- 8. 이동 수단 --%>
                <div class="question-step" data-question-key="transportation">
                    <h2>이동 수단🚗</h2>
                    <div class="card-options grid-2">
                        <div class="card" data-value="렌터카">렌터카</div>
                        <div class="card" data-value="대중교통">대중교통</div>
                    </div>
                </div>
        
                <%-- 9. 실내/실외 --%>
                <div class="question-step" data-question-key="activityType">
                    <h2>실내와 실외 중 더 선호하는곳☀️</h2>
                    <p class="description">여행 날짜의 날씨를 고려해 추천해드려요!</p>
                    <div class="card-options grid-3">
                        <div class="card" data-value="실내">실내</div>
                        <div class="card" data-value="실외">실외</div>
                        <div class="card" data-value="상관없음">상관없음</div>
                    </div>
                </div>
                
                <%-- 10. 동행인 --%>
                <div class="question-step" data-question-key="companion">
                    <h2>동행인👨‍👩‍👧‍👦</h2>
                    <div class="card-options grid-2">
                        <div class="card" data-value="혼자">혼자</div>
                        <div class="card" data-value="연인">연인</div>
                        <div class="card" data-value="가족">가족</div>
                        <div class="card" data-value="친구">친구</div>
                        <div class="card" data-value="아이 동반">아이동반</div>
                        <div class="card" data-value="어르신 동반">어르신 동반</div>
                    </div>
                </div>
            </div>
            
            <%-- 헬스케어 전용 질문 섹션 --%>
            <div id="healthcare-questions" style="display:none;">
                <div class="question-step" data-question-key="physicalInfo">
                    <h2>정확한 추천을 위해 정보를 입력해주세요.<span class="h2-emoji">🐻</span></h2>
                    <div class="form-inputs">
                        <c:choose>
                            <c:when test="${userInfo != null and userInfo.height != null}">
                                <select id="gender" class="input-field">
                                    <option value="남성" ${userInfo.gender == '남성' ? 'selected' : ''}>남성</option>
                                    <option value="여성" ${userInfo.gender == '여성' ? 'selected' : ''}>여성</option>
                                </select>
                                <input type="number" id="height" placeholder="키(cm)" class="input-field" value="${userInfo.height}">
                                <input type="number" id="weight" placeholder="몸무게(kg)" class="input-field" value="${userInfo.weight}">
                            </c:when>
                            <c:otherwise>
                                <select id="gender" class="input-field">
                                    <option value="남성">남성</option>
                                    <option value="여성">여성</option>
                                </select>
                                <input type="number" id="height" placeholder="키(cm)" class="input-field">
                                <input type="number" id="weight" placeholder="몸무게(kg)" class="input-field">
                            </c:otherwise>
                        </c:choose>
                        <button type="button" id="physicalInfoBtn" class="submit-btn">다음</button>
                    </div>
                </div>
                
                <div class="question-step" data-question-key="healthGoal">
                    <h2>건강목표🎯</h2>
                    <div class="card-options grid-2">
                        <div class="card" data-value="가볍게 걷기">가볍게 걷기</div>
                        <div class="card" data-value="적당한 활동">적당한 활동</div>
                        <div class="card" data-value="땀나는 활동">땀나는 활동</div>
                        <div class="card" data-value="고강도 활동">고강도 활동</div>
                    </div>
                </div>
                <div class="question-step" data-question-key="foodPreference">
                    <h2>음식 선호🥗</h2>
                    <div class="card-options grid-3">
                        <div class="card" data-value="건강식">건강식</div>
                        <div class="card" data-value="균형식">균형식</div>
                        <div class="card" data-value="고단백식">고단백식</div>
                    </div>
                </div>
                <div class="question-step" data-question-key="healthCondition">
                    <h2>특이 건강 고려🩺</h2>
                    <div class="card-options grid-3">
                        <div class="card" data-value="없음">없음</div>
                        <div class="card" data-value="고혈압">고혈압</div>
                        <div class="card" data-value="당뇨">당뇨</div>
                        <div class="card" data-value="무릎">무릎/관절</div>
                        <div class="card" data-value="기타">기타</div>
                    </div>
                </div>
            </div>
        </div>
        
        <div id="loading-container" style="display: none;">
            <div class="loading-content">
                <div class="loading-emoji">🤔</div>
                <p class="loading-text">잠시만 기다려 주세요...</p>
                <p class="loading-subtext">루트 만드는 중입니다</p>
            </div>
        </div>
    </main>
        
    <script>
    document.addEventListener('DOMContentLoaded', function() {
        
    	const userChoices = {};
        const progressBar = document.getElementById('progressBar');
        const generalQuestionContainer = document.getElementById('general-questions');
        const healthcareQuestionContainer = document.getElementById('healthcare-questions');
        const aiPlanContainer = document.getElementById('ai-plan-container');
        const loadingContainer = document.getElementById('loading-container');
        const dateSelectionStep = document.getElementById('date-selection-step');
        const dateSelectionGuide = document.getElementById('date-selection-guide');
        
        const calendarDates = document.getElementById('calendar-dates');
        const currentMonthYear = document.getElementById('current-month-year');
        const prevMonthBtn = document.getElementById('prev-month');
        const nextMonthBtn = document.getElementById('next-month');
        let currentDate = new Date();
        
        const totalGeneralSteps = 9;
        const totalHealthcareSteps = 4;
        
        function renderCalendar(year, month) {
            calendarDates.innerHTML = '';
            // ✅ [수정] JSP가 오해하지 않도록 문자열 합치기 방식으로 변경
            currentMonthYear.textContent = year + '년 ' + (month + 1) + '월';
            
            const firstDayOfMonth = new Date(year, month, 1).getDay();
            const lastDateOfMonth = new Date(year, month + 1, 0).getDate();
            const today = new Date();
            today.setHours(0, 0, 0, 0);

            for (let i = 0; i < firstDayOfMonth; i++) {
                calendarDates.insertAdjacentHTML('beforeend', '<div class="date-cell empty"></div>');
            }
            for (let date = 1; date <= lastDateOfMonth; date++) {
                const cellDate = new Date(year, month, date);
                let classes = 'date-cell';
                if (cellDate < today) {
                    classes += ' disabled';
                }
                // ✅ [수정] JSP가 오해하지 않도록 문자열 합치기 방식으로 변경
                const dateString = year + '-' + String(month + 1).padStart(2, '0') + '-' + String(date).padStart(2, '0');
                calendarDates.insertAdjacentHTML('beforeend', '<div class="' + classes + '" data-date="' + dateString + '">' + date + '</div>');
            }
        }

        function highlightDateRange() {
            if (!userChoices.startDate) return;
            const start = new Date(userChoices.startDate);
            const end = userChoices.endDate ? new Date(userChoices.endDate) : null;

            document.querySelectorAll('.date-cell[data-date]').forEach(cell => {
                const cellDate = new Date(cell.dataset.date);
                cell.classList.remove('selected-start', 'selected-end', 'in-range');

                if (cellDate.getTime() === start.getTime()) {
                    cell.classList.add('selected-start');
                }
                if (end && cellDate.getTime() === end.getTime()) {
                    cell.classList.add('selected-end');
                }
                if (end && cellDate > start && cellDate < end) {
                    cell.classList.add('in-range');
                }
            });
        }
        
        prevMonthBtn.addEventListener('click', () => {
            currentDate.setMonth(currentDate.getMonth() - 1);
            renderCalendar(currentDate.getFullYear(), currentDate.getMonth());
            highlightDateRange();
        });
        nextMonthBtn.addEventListener('click', () => {
            currentDate.setMonth(currentDate.getMonth() + 1);
            renderCalendar(currentDate.getFullYear(), currentDate.getMonth());
            highlightDateRange();
        });

        calendarDates.addEventListener('click', e => {
            const targetCell = e.target;
            if (!targetCell.matches('.date-cell:not(.empty):not(.disabled)')) return;

            const selectedDateStr = targetCell.dataset.date;
            const durationInNights = parseInt(userChoices.duration, 10);
            
            if (!userChoices.startDate || (durationInNights < 5 && userChoices.startDate)) {
                userChoices.startDate = selectedDateStr;
                userChoices.endDate = null;
                
                if (durationInNights >= 0 && durationInNights < 5) {
                    const startDate = new Date(selectedDateStr);
                    const endDate = new Date(startDate);
                    endDate.setDate(startDate.getDate() + durationInNights);
                    // ✅ [수정] JSP가 오해하지 않도록 문자열 합치기 방식으로 변경
                    userChoices.endDate = endDate.getFullYear() + '-' + String(endDate.getMonth() + 1).padStart(2, '0') + '-' + String(endDate.getDate()).padStart(2, '0');
                    
                    highlightDateRange();
                    setTimeout(() => showNextStep(dateSelectionStep), 500);
                } else {
                    dateSelectionGuide.textContent = "좋아요! 이제 여행 종료일을 선택해주세요.";
                    highlightDateRange();
                }
            } else if (durationInNights >= 5 && !userChoices.endDate) {
                const startDate = new Date(userChoices.startDate);
                const selectedEndDate = new Date(selectedDateStr);

                if (selectedEndDate <= startDate) {
                    alert("종료일은 시작일보다 이후 날짜여야 합니다. 시작일을 다시 선택해주세요.");
                    userChoices.startDate = null;
                    userChoices.endDate = null;
                    dateSelectionGuide.textContent = "여행 시작일을 선택해주세요.";
                    highlightDateRange();
                    return;
                }
                userChoices.endDate = selectedDateStr;
                highlightDateRange();
                setTimeout(() => showNextStep(dateSelectionStep), 500);
            }
        });
        
        function updateProgressBar() {
            let completedSteps = 0;
            const keys = Object.keys(userChoices);
            
            // 'duration'이 있으면 1, 'startDate'와 'endDate'가 모두 있으면 1 추가
            if (keys.includes('duration')) completedSteps++;
            if (keys.includes('startDate') && keys.includes('endDate')) completedSteps++;

            // 날짜 관련 키를 제외한 나머지 키 개수 추가
            const otherKeys = keys.filter(k => k !== 'duration' && k !== 'startDate' && k !== 'endDate');
            completedSteps += otherKeys.length;

            let totalSteps = userChoices.travelStyle === '헬스케어' ? totalGeneralSteps + totalHealthcareSteps : totalGeneralSteps;
            
            const progress = (completedSteps / totalSteps) * 100;
            progressBar.style.width = progress + '%';
        }
        
        function showNextStep(currentStepElement) {
            currentStepElement.classList.remove('active');
            const questionKey = currentStepElement.dataset.questionKey;

            if (questionKey === 'duration') {
                dateSelectionStep.classList.add('active');
                userChoices.startDate = null;
                userChoices.endDate = null;
                dateSelectionGuide.textContent = "여행 시작일을 선택해주세요.";
                renderCalendar(currentDate.getFullYear(), currentDate.getMonth());
                updateProgressBar();
                return;
            }

            if (questionKey === 'travelStyle' && userChoices.travelStyle === '헬스케어') {
                generalQuestionContainer.style.display = 'none';
                healthcareQuestionContainer.style.display = 'block';
                const firstHealthQuestion = healthcareQuestionContainer.querySelector('.question-step');
                if (firstHealthQuestion) {
                    firstHealthQuestion.classList.add('active');
                }
                updateProgressBar();
                return;
            }
            let nextStepElement = currentStepElement.nextElementSibling;
            if (!nextStepElement) {
                submitPlan();
                return;
            }
            nextStepElement.classList.add('active');
        }

        document.querySelectorAll('.card').forEach(card => {
            card.addEventListener('click', () => {
                const currentStepElement = card.closest('.question-step');
                const questionKey = currentStepElement.dataset.questionKey;
                const value = card.dataset.value;
                userChoices[questionKey] = value;
                updateProgressBar();
                currentStepElement.querySelectorAll('.card').forEach(c => c.classList.remove('selected'));
                card.classList.add('selected');
                setTimeout(() => showNextStep(currentStepElement), 400);
            });
        });

        document.getElementById('physicalInfoBtn').addEventListener('click', () => {
            const currentStepElement = document.querySelector('[data-question-key="physicalInfo"]');
           
            const gender = document.getElementById('gender').value;
            const height = document.getElementById('height').value;
            const weight = document.getElementById('weight').value;
            
            if (!height || !weight) {
                alert('키와 몸무게를 입력해주세요.');
                return;
            }
            
            userChoices.physicalInfo = {
                    gender: document.getElementById('gender').value,
                    height: height,
                    weight: weight
                };
            
            updateProgressBar();
            showNextStep(currentStepElement);
        });

        function submitPlan() {
            console.log('최종 사용자 선택:', userChoices);
            
            // 1. 로딩 화면을 보여줍니다.
            aiPlanContainer.style.display = 'none';
            loadingContainer.style.display = 'flex'; 
            progressBar.style.width = '100%';

            // 2. fetch API를 사용해 서버에 AJAX POST 요청을 보냅니다.
            fetch('/trip/ai/generate.do', { // ※※※※※ '/trip'은 본인의 프로젝트 경로(Context Path)에 맞게 수정하세요. ※※※※※
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userChoices) // JavaScript 객체를 JSON 문자열로 변환
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Server responded with a status: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                // 3. 서버로부터 성공 응답을 받으면, 지정된 URL로 페이지를 이동시킵니다.
                if (data.success) {
                    window.location.href = data.redirectUrl;
                } else {
                    // 서버 로직상 실패 시 (그럴 경우는 거의 없지만)
                    loadingContainer.style.display = 'none';
                    alert('루트 생성에 실패했습니다. 다시 시도해주세요.');
                }
            })
            .catch(error => {
                // 4. 네트워크 오류 등 요청 자체가 실패했을 때 처리합니다.
                console.error('Error:', error);
                loadingContainer.style.display = 'none';
                alert('서버와 통신 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.');
            });
        }
        renderCalendar(currentDate.getFullYear(), currentDate.getMonth());
    });
    </script>
</body>
</html>

