package org.iqpizza.gifbackground.configuration.constraint;

/**
 * 플러그인에서 계속해서 사용되는 상수들을 정의해놀은 클래스입니다.
 * 상수명이 `_MESSAGE`으로 끝난다면 컴포넌트에 사용되는 중복되는 문자열에 사용되며,
 * 그 외에는 설정값들을 저장하기 위한 키 값으로 사용됩니다.
 *
 * @since 0.1.0
 * @author iqpizza6349
 */
public class PluginConstraint {

    /** 배경화면 경로 */
    public static final String GIF_PATH = "GIF_PATH";

    /** 배경화면 설정 여부 */
    public static final String GIF_ENABLE = "IS_ENABLED";


    /** 불투명도 */
    public static final String TRANSPARENCY = "TRANSPARENCY";


    /** 불투명도 dialog 메시지 */
    public static final String TRANSPARENCY_MESSAGE = "Current transparency: %s";

    /** 배경화면 설정 dialog 메시지 */
    public static final String ENTER_GIF_MESSAGE = "Choose a gif file locally or enter a path";

}
