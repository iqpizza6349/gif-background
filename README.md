# GifBackground

기존에 있는 [GifBackground](https://github.com/BlueDriver/GifBackground) 플러그인을
새롭게 개량한 IntelliJ Plugin 입니다.

## 왜 새롭게 만들게 되었는 가?

기존 BlueDriver 의 플러그인에서 발견한 문제점
* 무겁다. 메모리 사용량이 상당히 높다.
* 사용자 친화적이지 않다. gif 파일 selector 가 없기에 직접 위치를 넣어줘야한다.

## 그럼 어떻게 개선할 것인가?

* BlueDriver 의 1 프레임마다 gif 을 출력하는 방식이 아닌 컴퓨터가 낼 수 있는 
    최대한의 크기를 청크 단위로 잘라서 출력하는 방법을 고안했다.
* 파일 selector 를 통해 기존보다 편하게 파일 위치를 찾을 수 있도록 한다.

## NOTE

해당 프로젝트는 BSD 라이센스입니다. 수정이나 삭제를 허용하지 않습니다.
