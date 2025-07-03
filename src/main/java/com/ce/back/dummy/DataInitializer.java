package com.ce.back.dummy;

import com.ce.back.entity.Game;
import com.ce.back.entity.PRGame;
import com.ce.back.entity.Team;
import com.ce.back.entity.User;
import com.ce.back.repository.GameRepository;
import com.ce.back.repository.PRGameRepository;
import com.ce.back.repository.TeamRepository;
import com.ce.back.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

        private final UserRepository userRepository;
        private final TeamRepository teamRepository;
        private final GameRepository matchRepository;
        private final PRGameRepository prGameRepository;

        public DataInitializer(UserRepository userRepository, TeamRepository teamRepository,
                        GameRepository matchRepository, PRGameRepository prGameRepository) {
                this.userRepository = userRepository;
                this.teamRepository = teamRepository;
                this.matchRepository = matchRepository;
                this.prGameRepository = prGameRepository;
        }

        @Override
        public void run(String... args) throws Exception {

                // 더미 사용자 생성
                User user1 = User.builder()
                                .userMail("LJG@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("이재균")
                                .tel("010-1234-5678")
                                .firstPosition("CAM")
                                .secondPosition("CM")
                                .thirdPosition("CDM")
                                .build();

                User user2 = User.builder()
                                .userMail("SYS@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("성윤수")
                                .tel("010-1234-5678")
                                .firstPosition("GK")
                                .secondPosition("CB")
                                .thirdPosition("ST")
                                .build();

                User user3 = User.builder()
                                .userMail("ADH@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("안도현")
                                .tel("010-1234-5678")
                                .firstPosition("LW")
                                .secondPosition("CDM")
                                .thirdPosition("RM")
                                .build();

                User user4 = User.builder()
                                .userMail("PJH@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("박주현")
                                .tel("010-1234-5678")
                                .firstPosition("LW")
                                .secondPosition("RW")
                                .thirdPosition("RB")
                                .build();

                User user5 = User.builder()
                                .userMail("YTG@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("윤태건")
                                .tel("010-1234-5678")
                                .firstPosition("SW")
                                .secondPosition("LCB")
                                .thirdPosition("RCB")
                                .build();

                User user6 = User.builder()
                                .userMail("JDJ@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("정동재")
                                .tel("010-1234-5678")
                                .firstPosition("LCM")
                                .secondPosition("RDM")
                                .thirdPosition("LAM")
                                .build();

                User user7 = User.builder()
                                .userMail("KWB@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("김웅빈")
                                .tel("010-1234-5678")
                                .firstPosition("RWB")
                                .secondPosition("RCB")
                                .thirdPosition("RW")
                                .build();

                User user8 = User.builder()
                                .userMail("OSM@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("오승민")
                                .tel("010-1234-5678")
                                .firstPosition("ST")
                                .secondPosition("CF")
                                .thirdPosition("LS")
                                .build();

                User user9 = User.builder()
                                .userMail("SJJ@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("서종진")
                                .tel("010-1234-5678")
                                .firstPosition("RS")
                                .secondPosition("RAM")
                                .thirdPosition("LAM")
                                .build();

                User user10 = User.builder()
                                .userMail("HONG@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("박재홍")
                                .tel("010-1234-5678")
                                .firstPosition("RW")
                                .secondPosition("RM")
                                .thirdPosition("RB")
                                .build();

                User user11 = User.builder()
                                .userMail("YSH@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("양상훈")
                                .tel("010-1234-5678")
                                .firstPosition("RCM")
                                .secondPosition("SW")
                                .thirdPosition("RDM")
                                .build();

                User user12 = User.builder()
                                .userMail("JSW@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("정선우")
                                .tel("010-1234-5678")
                                .firstPosition("LW")
                                .secondPosition("LS")
                                .thirdPosition("LAM")
                                .build();

                User user13 = User.builder()
                                .userMail("KBS@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("김보성")
                                .tel("010-1234-5678")
                                .firstPosition("LB")
                                .secondPosition("LWB")
                                .thirdPosition("LW")
                                .build();

                User user14 = User.builder()
                                .userMail("LYM@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("이영민")
                                .tel("010-1234-5678")
                                .firstPosition("LDM")
                                .secondPosition("RCM")
                                .thirdPosition("RAM")
                                .build();

                User user15 = User.builder()
                                .userMail("KJW@dankook.ac.kr")
                                .password("qwer1234")
                                .userName("김준원")
                                .tel("010-1234-5678")
                                .firstPosition("ST")
                                .secondPosition("CF")
                                .thirdPosition("RS")
                                .build();

                // 유저 저장 (중복 데이터 확인 후 삽입)
                saveUserIfNotExists(user1);
                saveUserIfNotExists(user2);
                saveUserIfNotExists(user3);
                saveUserIfNotExists(user4);
                saveUserIfNotExists(user5);
                saveUserIfNotExists(user6);
                saveUserIfNotExists(user7);
                saveUserIfNotExists(user8);
                saveUserIfNotExists(user9);
                saveUserIfNotExists(user10);
                saveUserIfNotExists(user11);
                saveUserIfNotExists(user12);
                saveUserIfNotExists(user13);
                saveUserIfNotExists(user14);
                saveUserIfNotExists(user15);

                // 더미 팀 생성
                Team team1 = Team.builder()
                                .teamName("불도저")
                                .location("용인")
                                .firstColor("red")
                                .secondColor("black")
                                .teamManager(user1) // 팀 관리자를 유저1로 설정
                                .users(Arrays.asList(user1, user2, user3, user4, user5,
                                                user6, user7, user8, user9, user10,
                                                user11, user12, user13, user14, user15)) // 유저를 팀에 포함
                                .build();

                Team team2 = Team.builder()
                                .teamName("포크레인")
                                .location("강남구 세곡동")
                                .firstColor("blue")
                                .secondColor("white")
                                .teamManager(user1) // 팀 관리자를 유저1로 설정
                                .users(Arrays.asList(user1, user2, user3, user7)) // 유저를 팀에 포함
                                .build();

                // 팀 저장 (중복 데이터 확인 후 삽입)
                saveTeamIfNotExists(team1);
                saveTeamIfNotExists(team2);

                // 더미 경기 생성
                Game match1 = Game.builder()
                                .gameName("불도저 1쿼터")
                                .date(LocalDateTime.of(2025, 5, 10, 14, 0))
                                .versus("바르셀로나")
                                .team(team1) // 불도저 팀
                                .players(Arrays.asList(user1, user2, user3, user4, user5,
                                                user6, user7, user8, user9, user10,
                                                user11, user12, user13, user14, user15)) // 경기에 참여할 유저들
                                .build();

                // 더미 경기 생성
                Game match2 = Game.builder()
                                .gameName("불도저 2쿼터")
                                .date(LocalDateTime.of(2025, 5, 10, 14, 30))
                                .versus("바르셀로나")
                                .team(team1) // 불도저 팀
                                .players(Arrays.asList(user1, user2, user3, user4, user5,
                                                user6, user7, user8, user9, user10,
                                                user11, user12, user13, user14, user15)) // 경기에 참여할 유저들
                                .build();

                // 더미 경기 생성
                Game match3 = Game.builder()
                                .gameName("포크레인 1쿼터")
                                .date(LocalDateTime.of(2025, 5, 10, 14, 0))
                                .versus("test")
                                .team(team2) // 포크레인 팀
                                .players(Arrays.asList(user1, user2, user3, user7)) // 경기에 참여할 유저들
                                .build();

                // 더미 경기 생성
                Game match4 = Game.builder()
                                .gameName("포크레인 2쿼터")
                                .date(LocalDateTime.of(2025, 5, 10, 14, 30))
                                .versus("test")
                                .team(team2) // 포크레인 팀
                                .players(Arrays.asList(user1, user2, user3, user7)) // 경기에 참여할 유저들
                                .build();

                // 경기 저장 (중복 데이터 확인 후 삽입)
                saveGameIfNotExists(match1);
                saveGameIfNotExists(match2);
                saveGameIfNotExists(match3);
                saveGameIfNotExists(match4);

                // 더미 PRGame 생성 예시
                PRGame pr1 = PRGame.builder()
                                .prGameName("prGame1")
                                .user(user2)
                                .game(match1)
                                .cam(user3)
                                .build();

                PRGame pr2 = PRGame.builder()
                                .prGameName("prGame2")
                                .user(user3)
                                .game(match1)
                                .gk(user1)
                                .build();

                // pr경기 저장 (중복 데이터 확인 후 삽입)
                savePRGameIfNotExists(pr1);
                savePRGameIfNotExists(pr2);

                System.out.println("더미 데이터가 성공적으로 생성되었습니다.");
        }

        // 유저가 중복되지 않으면 저장
        private void saveUserIfNotExists(User user) {
                Optional<User> existingUser = userRepository.findUserByUserMail(user.getUserMail());
                if (existingUser.isEmpty()) {
                        userRepository.save(user);
                }
        }

        // 팀이 중복되지 않으면 저장
        private void saveTeamIfNotExists(Team team) {
                List<Team> existingTeam = teamRepository.findTeamsByTeamName(team.getTeamName());
                if (existingTeam.isEmpty()) {
                        teamRepository.save(team);
                }
        }

        // 경기가 중복되지 않으면 저장
        private void saveGameIfNotExists(Game game) {
                List<Game> existingGame = matchRepository.findGamesByGameName(game.getGameName());
                if (existingGame.isEmpty()) {
                        matchRepository.save(game);
                }
        }

        // pr경기가 중복되지 않으면 저장
        private void savePRGameIfNotExists(PRGame prGame) {
                List<PRGame> existingPRGame = prGameRepository.findByPrGameName(prGame.getPrGameName());
                if (existingPRGame.isEmpty()) {
                        prGameRepository.save(prGame);
                }
        }
}