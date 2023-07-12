package org.iqpizza.gifbg.debug;

/**
 * 이 레코드 클래스는 디버깅 혹은 현재 gif 프레임 정보를
 * 지니고 있습니다.
 * @param delay 지연 시간
 * @param path gif 파일 경로
 *
 * @since 0.1.0
 * @author iqpizza6349
 */
public record FrameInfo(int delay, String path) {
}
