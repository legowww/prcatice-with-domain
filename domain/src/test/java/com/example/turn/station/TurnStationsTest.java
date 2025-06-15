package com.example.turn.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TurnStationsTest {

    @Nested
    @DisplayName("TurnStations 생성 테스트")
    class CreationTests {

        @Test
        @DisplayName("정상적인 정류장 목록으로 객체가 올바르게 생성된다")
        void createWithValidStationList() {
            // given
            List<TurnStation> validStations = createTurnStations();

            // when
            TurnStations turnStations = TurnStations.of(validStations);

            // then
            assertThat(turnStations).isNotNull();
            assertThat(validStations.size()).isEqualTo(turnStations.turnStations().size());

            // 일급 컬렉션에서 리스트를 반환하는건 불변성에 영향을 줄 수 있을듯함. 필요한 메서드로 캡슐화하는게 좋을듯
            assertThat(turnStations.turnStations()).isNotSameAs(validStations);
        }

        @Test
        @DisplayName("정류장 목록이 null이면 예외가 발생한다")
        void throwExceptionWhenStationsIsNull() {
            // when & then
            assertThatThrownBy(() -> TurnStations.of(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("정류장 목록은 null일 수 없습니다");
        }

        @Test
        @DisplayName("정류장이 한 개만 있으면 예외가 발생한다")
        void throwExceptionWhenOnlyOneStation() {
            // given
            TurnStation station = createTurnStation(1, TurnStationType.BEGIN, TurnStationOnOffType.ON);

            // when & then
            assertThatThrownBy(() -> TurnStations.of(Collections.singletonList(station)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정류장 목록은 두 개 이상이어야 합니다");
        }
    }

    @Nested
    @DisplayName("정류장 순서 검증 테스트")
    class OrderingValidationTests {

        @Test
        @DisplayName("정류장 순서가 0 이하인 경우 예외가 발생한다")
        void throwExceptionWhenOrderIsZeroOrNegative() {
            // given
            TurnStation beginStation = createTurnStation(0, TurnStationType.BEGIN, TurnStationOnOffType.ON);
            TurnStation endStation = createTurnStation(-1, TurnStationType.END, TurnStationOnOffType.OFF);

            // when & then
            assertThatThrownBy(() -> TurnStations.of(Arrays.asList(beginStation, endStation)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정류장 순서는 0보다 커야 합니다");
        }

        @Test
        @DisplayName("정류장 순서가 중복되면 예외가 발생한다")
        void throwExceptionWhenOrderIsDuplicated() {
            // given
            TurnStation beginStation = createTurnStation(1, TurnStationType.BEGIN, TurnStationOnOffType.ON);
            TurnStation midStation = createTurnStation(1, TurnStationType.MIDDLE, TurnStationOnOffType.ON);
            TurnStation endStation = createTurnStation(3, TurnStationType.END, TurnStationOnOffType.OFF);

            // when & then
            assertThatThrownBy(() -> TurnStations.of(Arrays.asList(beginStation, midStation, endStation)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정류장 순서가 중복되었습니다");
        }

        @Test
        @DisplayName("정류장 순서가 올바르게 정렬되지 않았다면 예외가 발생한다")
        void throwExceptionWhenOrderIsNotSorted() {
            // given
            TurnStation beginStation = createTurnStation(3, TurnStationType.BEGIN, TurnStationOnOffType.ON);
            TurnStation midStation = createTurnStation(2, TurnStationType.MIDDLE, TurnStationOnOffType.ON);
            TurnStation endStation = createTurnStation(1, TurnStationType.END, TurnStationOnOffType.OFF);

            // when & then
            assertThatThrownBy(() -> TurnStations.of(Arrays.asList(beginStation, midStation, endStation)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정류장 순서가 올바르지 않습니다");
        }
    }

    @Nested
    @DisplayName("시작/종료 정류장 검증 테스트")
    class BeginEndStationValidationTests {

        @Test
        @DisplayName("시작 정류장이 없으면 예외가 발생한다")
        void throwExceptionWhenNoBeginStation() {
            // given
            TurnStation station1 = createTurnStation(1, TurnStationType.MIDDLE, TurnStationOnOffType.ON);
            TurnStation station2 = createTurnStation(2, TurnStationType.MIDDLE, TurnStationOnOffType.OFF);
            TurnStation station3 = createTurnStation(3, TurnStationType.END, TurnStationOnOffType.OFF);

            // when & then
            assertThatThrownBy(() -> TurnStations.of(Arrays.asList(station1, station2, station3)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정류장 목록에 시작 정류장이 하나만 존재해야 합니다");
        }

        @Test
        @DisplayName("종료 정류장이 없으면 예외가 발생한다")
        void throwExceptionWhenNoEndStation() {
            // given
            TurnStation station1 = createTurnStation(1, TurnStationType.BEGIN, TurnStationOnOffType.ON);
            TurnStation station2 = createTurnStation(2, TurnStationType.MIDDLE, TurnStationOnOffType.OFF);
            TurnStation station3 = createTurnStation(3, TurnStationType.MIDDLE, TurnStationOnOffType.OFF);

            // when & then
            assertThatThrownBy(() -> TurnStations.of(Arrays.asList(station1, station2, station3)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정류장 목록에 종료 정류장이 하나만 존재해야 합니다");
        }

        @Test
        @DisplayName("시작 정류장이 여러 개면 예외가 발생한다")
        void throwExceptionWhenMultipleBeginStations() {
            // given
            TurnStation station1 = createTurnStation(1, TurnStationType.BEGIN, TurnStationOnOffType.ON);
            TurnStation station2 = createTurnStation(2, TurnStationType.BEGIN, TurnStationOnOffType.ON);
            TurnStation station3 = createTurnStation(3, TurnStationType.END, TurnStationOnOffType.OFF);

            // when & then
            assertThatThrownBy(() -> TurnStations.of(Arrays.asList(station1, station2, station3)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정류장 목록에 시작 정류장이 하나만 존재해야 합니다");
        }

        @Test
        @DisplayName("종료 정류장이 여러 개면 예외가 발생한다")
        void throwExceptionWhenMultipleEndStations() {
            // given
            TurnStation station1 = createTurnStation(1, TurnStationType.BEGIN, TurnStationOnOffType.ON);
            TurnStation station2 = createTurnStation(2, TurnStationType.END, TurnStationOnOffType.OFF);
            TurnStation station3 = createTurnStation(3, TurnStationType.END, TurnStationOnOffType.OFF);

            // when & then
            assertThatThrownBy(() -> TurnStations.of(Arrays.asList(station1, station2, station3)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정류장 목록에 종료 정류장이 하나만 존재해야 합니다");
        }

        @Test
        @DisplayName("첫 번째 정류장이 시작 정류장이 아니면 예외가 발생한다")
        void throwExceptionWhenFirstStationIsNotBegin() {
            // given
            TurnStation station1 = createTurnStation(1, TurnStationType.MIDDLE, TurnStationOnOffType.ON);
            TurnStation station2 = createTurnStation(2, TurnStationType.BEGIN, TurnStationOnOffType.ON);
            TurnStation station3 = createTurnStation(3, TurnStationType.END, TurnStationOnOffType.OFF);

            // when & then
            assertThatThrownBy(() -> TurnStations.of(Arrays.asList(station1, station2, station3)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정류장 목록의 첫 번째 정류장은 시작 정류장이어야 합니다");
        }

        @Test
        @DisplayName("마지막 정류장이 종료 정류장이 아니면 예외가 발생한다")
        void throwExceptionWhenLastStationIsNotEnd() {
            // given
            TurnStation station1 = createTurnStation(1, TurnStationType.BEGIN, TurnStationOnOffType.ON);
            TurnStation station2 = createTurnStation(2, TurnStationType.END, TurnStationOnOffType.OFF);
            TurnStation station3 = createTurnStation(3, TurnStationType.MIDDLE, TurnStationOnOffType.OFF);

            // when & then
            assertThatThrownBy(() -> TurnStations.of(Arrays.asList(station1, station2, station3)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정류장 목록의 마지막 정류장은 종료 정류장이어야 합니다");
        }
    }

    @Nested
    @DisplayName("승하차 유형 검증 테스트")
    class OnOffTypeValidationTests {

        @Test
        @DisplayName("첫 번째 정류장이 승차 정류장이 아니면 예외가 발생한다")
        void throwExceptionWhenFirstStationIsNotOnType() {
            // given
            TurnStation beginStation = createTurnStation(1, TurnStationType.BEGIN, TurnStationOnOffType.OFF);
            TurnStation endStation = createTurnStation(2, TurnStationType.END, TurnStationOnOffType.OFF);

            // when & then
            assertThatThrownBy(() -> TurnStations.of(Arrays.asList(beginStation, endStation)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정류장 목록의 첫 번째 정류장은 시작 정류장이어야 합니다");
        }

        @Test
        @DisplayName("마지막 정류장이 하차 정류장이 아니면 예외가 발생한다")
        void throwExceptionWhenLastStationIsNotOffType() {
            // given
            TurnStation beginStation = createTurnStation(1, TurnStationType.BEGIN, TurnStationOnOffType.ON);
            TurnStation endStation = createTurnStation(2, TurnStationType.END, TurnStationOnOffType.ON);

            // when & then
            assertThatThrownBy(() -> TurnStations.of(Arrays.asList(beginStation, endStation)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("정류장 목록의 마지막 정류장은 종료 정류장이어야 합니다");
        }
    }

    @Nested
    @DisplayName("기능 테스트")
    class FunctionalityTests {

        @Test
        @DisplayName("시작 정류장을 올바르게 반환한다")
        void getBeginStationCorrectly() {
            // given
            List<TurnStation> stations = createTurnStations();
            TurnStations turnStations = TurnStations.of(stations);

            // when
            TurnStation beginStation = turnStations.getBeginStation();

            // then
            assertThat(TurnStationType.BEGIN).isEqualTo(beginStation.getPointType());
            assertThat(TurnStationOnOffType.ON).isEqualTo(beginStation.getOnOffType());
        }

        @Test
        @DisplayName("종료 정류장을 올바르게 반환한다")
        void getEndStationCorrectly() {
            // given
            List<TurnStation> stations = createTurnStations();
            TurnStations turnStations = TurnStations.of(stations);

            // when
            TurnStation endStation = turnStations.getEndStation();

            // then
            assertThat(TurnStationType.END).isEqualTo(endStation.getPointType());
            assertThat(TurnStationOnOffType.OFF).isEqualTo(endStation.getOnOffType());
        }

        @Test
        @DisplayName("시작 정류장 예상 도착 시간을 올바르게 반환한다")
        void getBeginStationExpectedArrivalTimeCorrectly() {
            // given
            List<TurnStation> stations = createTurnStations();
            TurnStations turnStations = TurnStations.of(stations);
            OffsetDateTime expectedTime = stations.get(0).getExpectedArrivalTime();

            // when
            OffsetDateTime actualTime = turnStations.getBeginStationExpectedArrivalTime();

            // then
            assertThat(expectedTime).isEqualTo(actualTime);
        }

        @Test
        @DisplayName("종료 정류장 예상 도착 시간을 올바르게 반환한다")
        void getEndStationExpectedArrivalTimeCorrectly() {
            // given
            List<TurnStation> stations = createTurnStations();
            TurnStations turnStations = TurnStations.of(stations);
            OffsetDateTime expectedTime = stations.get(stations.size() - 1).getExpectedArrivalTime();

            // when
            OffsetDateTime actualTime = turnStations.getEndStationExpectedArrivalTime();

            // then
            assertThat(expectedTime).isEqualTo(actualTime);
        }
    }

    @Nested
    @DisplayName("불변성 테스트")
    class ImmutabilityTests {

        @Test
        @DisplayName("원본 리스트 변경이 TurnStations에 영향을 주지 않는다")
        void originalListModificationDoesNotAffectTurnStations() {
            // given
            List<TurnStation> originalStations = new ArrayList<>(createTurnStations());
            TurnStations turnStations = TurnStations.of(originalStations);

            // when - 원본 리스트 수정 시도
            TurnStation newStation = createTurnStation(4, TurnStationType.BEGIN, TurnStationOnOffType.ON);
            originalStations.add(newStation);

            // then
            assertThat(originalStations.size()).isEqualTo(4);
            assertThat(turnStations.turnStations().size()).isEqualTo(3);
        }

        @Test
        @DisplayName("TurnStations의 리스트의 원소값을 추가할 수 없다")
        void turnStationsListIsUnmodifiableAdd() {
            // given
            TurnStations turnStations = TurnStations.of(createTurnStations());

            // when & then
            assertThatThrownBy(() -> {
                List<TurnStation> stations = turnStations.turnStations();
                stations.add(createTurnStation(4, TurnStationType.BEGIN, TurnStationOnOffType.ON));
            }).isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("TurnStations의 리스트 원소값을 제거할 수 없다")
        void turnStationsListIsUnmodifiableRemove() {
            // given
            TurnStations turnStations = TurnStations.of(createTurnStations());

            // when & then
            assertThatThrownBy(() -> {
                List<TurnStation> stations = turnStations.turnStations();
                stations.remove(0);
            }).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    private List<TurnStation> createTurnStations() {
        TurnStation beginStation = createTurnStation(1, TurnStationType.BEGIN, TurnStationOnOffType.ON);
        TurnStation midStation = createTurnStation(2, TurnStationType.MIDDLE, TurnStationOnOffType.ON);
        TurnStation endStation = createTurnStation(3, TurnStationType.END, TurnStationOnOffType.OFF);

        return Arrays.asList(beginStation, midStation, endStation);
    }

    private TurnStation createTurnStation(int order, TurnStationType pointType, TurnStationOnOffType onOffType) {
        TurnStation turnStation = mock(TurnStation.class);

        when(turnStation.getOrder()).thenReturn(order);
        when(turnStation.getPointType()).thenReturn(pointType);
        when(turnStation.getOnOffType()).thenReturn(onOffType);
        when(turnStation.getExpectedArrivalTime()).thenReturn(OffsetDateTime.now().plusMinutes(order * 10));

        return turnStation;
    }
}
