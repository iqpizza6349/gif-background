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

    public static final int DEFAULT_MAX_VIDEO_WIDTH = 960;
    public static final int DEFAULT_MAX_VIDEO_HEIGHT = 540;


    /** 배경화면 경로 */
    public static final String GIF_PATH = "GIF_PATH";

    /** 배경화면 설정 여부 */
    public static final String GIF_ENABLE = "IS_ENABLED";


    /** 불투명도 */
    public static final String TRANSPARENCY = "TRANSPARENCY";

    public static final Float DEFAULT_TRANSPARENCY = 1F;


    /** 불투명도 dialog 메시지 */
    public static final String TRANSPARENCY_MESSAGE = "Current transparency: %s";

    /** 배경화면 설정 dialog 메시지 */
    public static final String ENTER_GIF_MESSAGE = "Choose a gif file locally or enter a path";



    public static final int DEFAULT_PROGRESS_BAR_MAXIMUM = 1000;
    public static final int DEFAULT_PROGRESS_BAR_HEIGHT = 2;
    public static final int DEFAULT_PROGRESS_BAR_INDICATOR_SIZE = 10;
    public static final int DEFAULT_PROGRESS_BAR_INDICATOR_COLOR = 0xFE4CAF50;
    public static final int DEFAULT_PROGRESS_BAR_PRIMARY_COLOR = 0x804CAF50;
    public static final int DEFAULT_PROGRESS_BAR_SECONDARY_COLOR = 0x50A5D6A7;


    public static final String GRABBER_ERROR_MESSAGE = "Grab Error!";
    /** */
    public static final String GRABBER_INITIALIZATION_FAILED_MESSAGE = "Grabber initialization failed!";

    public static final String PAINTER_INITIALIZATION_FAILED_MESSAGE = "Background Painter initialization failed!";

}
