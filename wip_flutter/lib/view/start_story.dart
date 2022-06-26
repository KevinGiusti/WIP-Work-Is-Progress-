import 'package:flutter/material.dart';

class StartStory extends StatefulWidget {
  const StartStory({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<StartStory> createState() => _StartStoryState();

}

class _StartStoryState extends State<StartStory> {
  double _studyTime = 20;
  double _pause = 40;
  final double _maxSlider = 120;
  bool _silenceMode = false;
  bool _hardcoreMode = false;

  String imagesPath = 'assets/images/';

  String backButtonPath = 'assets/images/back_button.png';
  String infoButtonPath = 'assets/images/info_button.png';
  String sxButtonPath = 'assets/images/avatar_sx_arrow.png';
  String dxButtonPath = 'assets/images/avatar_dx_arrow.png';
  String startStoryButtonPath = 'assets/images/start_story_button.png';

  late Image backButtonPressed;
  late Image infoButtonPressed;
  late Image sxButtonPressed;
  late Image dxButtonPressed;
  late Image startStoryButtonPressed;

  @override
  void initState() {
    super.initState();
    backButtonPressed = Image.asset('${imagesPath}back_button_pressed.png');
    infoButtonPressed = Image.asset('${imagesPath}info_button_pressed.png');
    sxButtonPressed = Image.asset('${imagesPath}avatar_sx_arrow_pressed.png');
    dxButtonPressed = Image.asset('${imagesPath}avatar_dx_arrow_pressed.png');
    startStoryButtonPressed = Image.asset('${imagesPath}start_story_button_pressed.png');
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    precacheImage(backButtonPressed.image, context);
    precacheImage(infoButtonPressed.image, context);
    precacheImage(sxButtonPressed.image, context);
    precacheImage(dxButtonPressed.image, context);
    precacheImage(startStoryButtonPressed.image, context);
  }

  void setBackButtonPath(String backButtonPath) {
    setState(() {
      this.backButtonPath = imagesPath + backButtonPath;
    });
  }

  void setInfoButtonPath(String infoButtonPath) {
    setState(() {
      this.infoButtonPath = imagesPath + infoButtonPath;
    });
  }

  void setSxButtonPath(String sxButtonPath) {
    setState(() {
      this.sxButtonPath = imagesPath + sxButtonPath;
    });
  }

  void setDxButtonPath(String dxButtonPath) {
    setState(() {
      this.dxButtonPath = imagesPath + dxButtonPath;
    });
  }

  void setStartStoryButtonPath(String startStoryButtonPath) {
    setState(() {
      this.startStoryButtonPath = imagesPath + startStoryButtonPath;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
          body: Container(
            decoration: const BoxDecoration(
                image: DecorationImage(
                    image: AssetImage('assets/images/background.png'),
                    fit: BoxFit.fill
                )
            ),
            padding: const EdgeInsets.only(top: 30, bottom: 30, left: 20, right: 20),
            child: Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Align(
                    alignment: Alignment.centerLeft,
                    child: GestureDetector(
                        onTapDown: (_){
                          setBackButtonPath('back_button_pressed.png');
                        },
                        onPanDown: (_){
                          setBackButtonPath('back_button_pressed.png');
                        },
                        onTapCancel: (){
                          setBackButtonPath('back_button.png');
                        },
                        onPanEnd: (_){
                          setBackButtonPath('back_button.png');
                        },
                        onTap: () {
                          Navigator.pop(context);
                          setBackButtonPath('back_button.png');
                        },
                      child: Image.asset(backButtonPath, height: 60, width: 60, fit: BoxFit.cover)
                    )
                  ),
                  const Text(
                    'Titolo',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                        color: Colors.white,
                        fontFamily: 'PressStart2P',
                        fontSize: 22
                    ),
                  ),
                  Container(
                    height: 70,
                    decoration: const BoxDecoration(
                      image: DecorationImage(
                        image: AssetImage('assets/images/rectangular_background.png'),
                        fit: BoxFit.fill
                      )
                    ),
                    padding: const EdgeInsets.only(left: 20, right: 20),
                    child: const Center(
                        child: TextField(
                          decoration: InputDecoration(
                              border: InputBorder.none,
                              hintText: 'Nuova storia',
                              hintStyle: TextStyle(
                                  color: Color.fromARGB(255, 2, 119, 189),
                                  fontFamily: 'PressStart2P',
                                  fontSize: 16
                              )
                          ),
                          style: TextStyle(
                              color: Color.fromARGB(255, 2, 119, 189),
                              fontFamily: 'PressStart2P',
                              fontSize: 16
                          )
                        )
                    ),
                  ),
                  const Text(
                    'Tempo della storia',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                        color: Colors.white,
                        fontFamily: 'PressStart2P',
                        fontSize: 22
                    ),
                  ),
                  Container(
                    height: 70,
                    decoration: const BoxDecoration(
                        image: DecorationImage(
                            image: AssetImage('assets/images/rectangular_background.png'),
                            fit: BoxFit.fill
                        )
                    ),
                    child: Center(
                        child: Slider(
                          thumbColor: const Color.fromARGB(255, 2, 119, 189),
                          min: 10.0,
                          max: _maxSlider - 10,
                          divisions: _maxSlider~/10 - 2, //Numero di divisioni - 1 sinistra e -1 destra
                          label: '${_studyTime.round()} min lavoro/${_pause.round()} min pausa',
                          value: _studyTime,
                          onChanged: (newStudyTime) {
                            setState(() {
                              _studyTime = newStudyTime;
                              _pause = _maxSlider - _studyTime;
                            });
                          },
                        )
                    ),
                  ),
                  const Text(
                    'Impost. storia',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                        color: Colors.white,
                        fontFamily: 'PressStart2P',
                        fontSize: 22
                    ),
                  ),
                  Container(
                    height: 110,
                    decoration: const BoxDecoration(
                        image: DecorationImage(
                            image: AssetImage('assets/images/rectangular_background.png'),
                            fit: BoxFit.fill
                        )
                    ),
                    padding: const EdgeInsets.only(left: 20),
                    child: Row(
                      children: [
                        Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Row(
                              children: [const Text(
                                'Mod. silenzio',
                                style: TextStyle(
                                    color: Color.fromARGB(255, 2, 119, 189),
                                    fontFamily: 'PressStart2P',
                                    fontSize: 16,
                                ),
                              ),
                                Switch(
                                    value: _silenceMode,
                                    onChanged: (value) {
                                      setState((){
                                            if(!_hardcoreMode) {
                                              _silenceMode = value;
                                            }
                                          }
                                      );
                                    }
                                )
                              ]
                            ),
                            Row(
                              children: [
                                const Text(
                                  'Mod. hardcore',
                                  style: TextStyle(
                                      color: Color.fromARGB(255, 2, 119, 189),
                                      fontFamily: 'PressStart2P',
                                      fontSize: 16,
                                  ),
                                ),
                                Switch(
                                    value: _hardcoreMode,
                                    onChanged: (value) {
                                      setState((){
                                        _silenceMode = value;
                                        _hardcoreMode = value;
                                      }
                                      );
                                    }
                                )
                              ],
                            )
                          ],
                        ),
                        Align(
                          alignment: Alignment.topCenter,
                          child: GestureDetector(
                              onTapDown: (_){
                                setInfoButtonPath('info_button_pressed.png');
                              },
                              onPanDown: (_){
                                setInfoButtonPath('info_button_pressed.png');
                              },
                              onTapCancel: (){
                                setInfoButtonPath('info_button.png');
                              },
                              onPanEnd: (_){
                                setInfoButtonPath('info_button.png');
                              },
                            child: IconButton(
                              icon: Image.asset(infoButtonPath),
                              onPressed: () {
                                setInfoButtonPath('info_button.png');
                              }
                            )
                          )
                        )
                      ],
                    )
                  ),
                  const Text(
                    'Scegli avatar',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                        color: Colors.white,
                        fontFamily: 'PressStart2P',
                        fontSize: 22
                    ),
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      GestureDetector(
                          onTapDown: (_){
                            setSxButtonPath('avatar_sx_arrow_pressed.png');
                          },
                          onPanDown: (_){
                            setSxButtonPath('avatar_sx_arrow_pressed.png');
                          },
                          onTapCancel: (){
                            setSxButtonPath('avatar_sx_arrow.png');
                          },
                          onPanEnd: (_){
                            setSxButtonPath('avatar_sx_arrow.png');
                          },
                          child: IconButton(
                              padding: const EdgeInsets.only(right: 30),
                              icon: Image.asset(sxButtonPath),
                              iconSize: 50,
                              onPressed: () {
                                setSxButtonPath('avatar_sx_arrow.png');
                              }
                          )
                      ),
                      SizedBox(
                          height: 100,
                          width: 60,
                          child: FittedBox(
                              fit: BoxFit.cover,
                              child: Image.asset('assets/images/venere.png'),
                          )
                      ),
                      GestureDetector(
                          onTapDown: (_){
                            setDxButtonPath('avatar_dx_arrow_pressed.png');
                          },
                          onPanDown: (_){
                            setDxButtonPath('avatar_dx_arrow_pressed.png');
                          },
                          onTapCancel: (){
                            setDxButtonPath('avatar_dx_arrow.png');
                          },
                          onPanEnd: (_){
                            setDxButtonPath('avatar_dx_arrow.png');
                          },
                        child: IconButton(
                            padding: const EdgeInsets.only(left: 30),
                            icon: Image.asset(dxButtonPath),
                            iconSize: 50,
                            onPressed: () {
                              setDxButtonPath('avatar_dx_arrow.png');
                            }
                        )
                      )
                    ],
                  ),
                  GestureDetector(
                      onTapDown: (_){
                        setStartStoryButtonPath('start_story_button_pressed.png');
                      },
                      onPanDown: (_){
                        setStartStoryButtonPath('start_story_button_pressed.png');
                      },
                      onTapCancel: (){
                        setStartStoryButtonPath('start_story_button.png');
                      },
                      onPanEnd: (_){
                        setStartStoryButtonPath('start_story_button.png');
                      },
                    onTap: () {
                      Navigator.pushNamed(context, '/story-started');
                      setStartStoryButtonPath('start_story_button.png');
                    },
                    child: Image.asset(startStoryButtonPath)
                  )

                ]
              ),
            ),
          ),
      resizeToAvoidBottomInset: false,
    );
  }
}