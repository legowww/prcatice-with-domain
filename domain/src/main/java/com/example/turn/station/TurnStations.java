package com.example.turn.station;


import java.time.OffsetDateTime;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 정류장 목록을 관리하는 일급 컬렉션 클래스
 * 테스트 코드: TurnStationsTest
 */
public record TurnStations(List<TurnStation> turnStations) {

    public static TurnStations of(List<TurnStation> turnStations) {
        return new TurnStations(turnStations);
    }

    public TurnStations(List<TurnStation> turnStations) {
        validate(turnStations);
        // turnStations 의 내부 원소들은 같은 주소값임. 이 값들도 복사해서 다른 주소값을 가지도록 하는 방법 고민해볼만함.
        this.turnStations = List.copyOf(turnStations);
    }


    public TurnStation getBeginStation() {
        return this.turnStations.getFirst();
    }


    public OffsetDateTime getBeginStationExpectedArrivalTime() {
        return this.turnStations.getFirst().getExpectedArrivalTime();
    }

    public TurnStation getEndStation() {
        return this.turnStations.getLast();
    }


    public OffsetDateTime getEndStationExpectedArrivalTime() {
        return this.turnStations.getLast().getExpectedArrivalTime();
    }

    private void validate(List<TurnStation> turnStations) {
        checkNotNull(turnStations, "정류장 목록은 null일 수 없습니다.");
        checkArgument(turnStations.size() >= 2, "정류장 목록은 두 개 이상이어야 합니다.");
        validateStationOrdering(turnStations);
        validateBeginAndEndStationExistOnlyOne(turnStations);
        validateBeginAndEndStationIsOnAndOffStation(turnStations);
    }

    private void validateStationOrdering(List<TurnStation> turnStations) {
        List<Integer> ordering = turnStations.stream().map(TurnStation::getOrder).toList();
        List<Integer> sortedOrdering = ordering.stream().distinct().sorted().toList();

        if (sortedOrdering.getFirst() <= 0) {
            throw new IllegalArgumentException("정류장 순서는 0보다 커야 합니다.");
        }

        if (ordering.size() != sortedOrdering.size()) {
            throw new IllegalArgumentException("정류장 순서가 중복되었습니다.");
        }

        if (!ordering.equals(sortedOrdering)) {
            throw new IllegalArgumentException("정류장 순서가 올바르지 않습니다.");
        }
    }

    private void validateBeginAndEndStationExistOnlyOne(List<TurnStation> turnStations) {
        long beginCount = turnStations.stream()
                .filter(turnStation -> turnStation.getPointType() == TurnStationPointType.BEGIN)
                .count();

        long endCount = turnStations.stream()
                .filter(turnStation -> turnStation.getPointType() == TurnStationPointType.END)
                .count();

        if (beginCount != 1) {
            throw new IllegalArgumentException("정류장 목록에 시작 정류장이 하나만 존재해야 합니다.");
        }

        if (endCount != 1) {
            throw new IllegalArgumentException("정류장 목록에 종료 정류장이 하나만 존재해야 합니다.");
        }

        TurnStation beginStation = turnStations.getFirst();
        TurnStation endStation = turnStations.getLast();

        if (beginStation.getPointType() != TurnStationPointType.BEGIN) {
            throw new IllegalArgumentException("정류장 목록의 첫 번째 정류장은 시작 정류장이어야 합니다.");
        }

        if (endStation.getPointType() != TurnStationPointType.END) {
            throw new IllegalArgumentException("정류장 목록의 마지막 정류장은 종료 정류장이어야 합니다.");
        }
    }

    private void validateBeginAndEndStationIsOnAndOffStation(List<TurnStation> turnStations) {
        if (turnStations.getFirst().getOnOffType() != TurnStationOnOffType.ON) {
            throw new IllegalArgumentException("정류장 목록의 첫 번째 정류장은 시작 정류장이어야 합니다.");
        }

        if (turnStations.getLast().getOnOffType() != TurnStationOnOffType.OFF) {
            throw new IllegalArgumentException("정류장 목록의 마지막 정류장은 종료 정류장이어야 합니다.");
        }
    }
}
