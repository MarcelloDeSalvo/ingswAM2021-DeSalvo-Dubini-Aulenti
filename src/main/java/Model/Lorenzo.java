package Model;

import java.util.ArrayList;
import java.util.Collections;

public class Lorenzo {
    private ArrayList<ActionToken> actionTokens;

    public Lorenzo() {
    }

    public Lorenzo(ArrayList<ActionToken> actionTokens) {
        this.actionTokens = actionTokens;
        Collections.shuffle(this.actionTokens);
    }

    public void shuffleActionTokens() {
        Collections.shuffle(actionTokens);
    }

    public void pickAction(Game g) {
        for (Action action : actionTokens.get(0).getActions()) {
            action.doAction(g);
        }

        actionTokens.add(actionTokens.get(0));
        actionTokens.remove(0);
    }
}

























/*

                                              ......................................................
                                              ......................................................
                                             .........................................................
                                            ..........................................................
                                            .........................................................
                                            .............................'...;c,.....................
                                             .......'..................'::'.;lo:''..''...,'.'........
                                             ......:l'.;c'.'...,,....':lo:;:lool;:c'':;..lo;:,.....
                                             .....'oc.cxl,:'.':l'.,,;looollooooooloc,:l,'okooc....
                                              ....'o:ckxccc';loc',lloooooooooloooooolcoc:odxk:....
                                               ...'ldkkxooc:oooc:looooloolllollllooooddxxxxkOc...'.
                                             .:c,..:kOxxxxxxxkkkxddoooloolloollodxkkOO00OOkk0o''lxd;
                                            .cdxxc.;kOolccodxOO00OOkkxdxkdxkxxkO0000K0kdc;;dkc,ldldl.
                                            'oxxdd;'lkxlloxkO0KXXKKOocoxxko:cd0KXXNNNNX0d;cko';ocok:.
                                          . ,kkdloc.;okOKNWMMMMWMMWW0ddlcodoxXWMMMMMMMMMXxdo;':oldl.
                                            .lxlldl,lddkXWMMMMMWMMWMNkloollldKWMMMMMMMMMXkddo::llc. .  .
                                             .loldl'l0000KXNWWWWWWNKkdxkOxodolok0KXXXXK0kk00d;clc'     .
                                      ..   .  .lodo';k000OkOOOOkddkkxkkkOdlolc::lxO0OOOOO00x;.:dc.  .   .
                                         ..    ,dxo'.:xO0000O00Okk0OkkOOOkoollloodkOOOOOOOx;.,xd'       .
                                               .;okc.'okkO0OOOOOO00kxOOkOOollc,;looooddoodc. .,'.      .
                           ..                 ..',;,..;xkxxkkxdxdlcooooxOOl;;::;,coooooooo;. ..'..  .  .
                                              ..''..  .;oolloololcodddloxdddxxxoccoooooooc.  .,,'. ..                              .
                   ..                        . .'''.  ..,lodkOO00KKKKKK000000KKK0kxdooloo;...',,..                               .     ..
                                      ..   . . ..'''....;dOKXXXXKK00K0KXXXXXKK00KKKK00Okxc...',.. .                                 ..       ..
             .                         .  .. ..';clccldOKXXK0KK000KKXXXXKKKKXXXXK000000000xllll:,,'..   .   .. ..                            ...
             .                         . ..:ldxkOO00000KXXX0O0KXKKKK0OkO000000KK00KKXXXKOOOO0000KKKkl,.....  .     .                          ..
     .       .                  ..     .;okO0OOOOOO00OO0XXXX0OO0O000OO0XXXNNNXXXKK0000000KXXXK00KXKKKKOxoc:,'...      ...                      ..
     .                             ..,cxKXOkOOO0O0KKXXXXXXXXXKKXXXXNXXK0000KKXXXXXXXXXXK0000KXXXXX0OO0000K00Okdl;'..   .             ...  .  . ...
     ..                     .   .'cdO0XXXXKO0KXXXXXXK000OOKXXXXXXKOOOOO000000OO00000O000KXX0OKXXXXXXXK00OOOOO0OOkkkd:.                         ...
  ..  .                  .  .  'dKXXXXXXXXXXXXXX0OOOOOOOO0KXXK0K00O0XXXXXXNNNXXXNNNNXK0OO0OkOKXXXKKXXXXXXXXXXKK000XXXOl,..   .                 ....
  ..                      .  .;kXXXXXXXXXNXK0OOOOOKXXXXXXXXXXO0XXKKKKKK000KXXK00KXXNNWWNXXKKKXXXX0OOOOOOOOOO000KXXXXXXXKOd:..   .            ......
  ...                    . .,o0XXXXXXXXXXNNK0OKXXXXXXXXXXXXNN0OOOOOdc;,...',,'..';cokKX0KXXXXXXXXXXXXKK00OOOOOOO0XXXXXXXXXXOo.              .......
  ...              .   . .;d0XXXXNXXNNWWWWWWWWNNNNNNNNNNNWNWWWXXXNX0xl;..      ...  .;x0KXXXXXNNNNNNNNNXXNNNNNNNNXXXXXXXXXXXO;      .       ........
 .....               ....c0XXXXXNWNNWWWWWWWWWWWWWWWWWWNXXXXKXXKXXXXNWWNX0ko:;''''..'cxKNNNNNWWWWWWWWWWWWWWWWWWMWWNNNNXXXXXXKxc,. ..  ..     ........
 .....          .........'oKXXXXNWWWWWWMWWWNNNNNNXXNNXKOxoccooloddxk0XXXNWNXKxlldxOKNXXXXXKXXXXNWWMMWWWWWWMWWWMWWWWMWNNXXXKxooo:....     .  ........
 ........      ..........':dOXXXNWWWWWWWNNKOKXXXK0Okxo:,....'''''',;:loxOKNXNWNXK0000OOkolodxdllok0XNNNNNNWWWWWWNNWNXXWWNXkoooooc.......   .  .......
..........   ...........'cooox0XNWWWWMWWWN0OKkoc:;'.........',,,,'',,',ck00OOKXNNK0xloo,..........,:oxOKXXNXK0K0O0KXXNWWWKxdooooo:.......  ..........
.......................,coooolokKNMWWWWWWXKXXc.............',,'''',',ckKXKOkdllxOOkl;cl,..............,:cllokK0OOKNWWWWWXOOkdooool;'..................
.....................'';looooooxk0KNNWNXOk00O:...............'',',,',cddol:;,,,:c:;:;;l;....................lKWNXXKXWNXKOO0Okdooool;''................
.....................''';clooox00OOO0Okl,'''...................',,''''',''',,:c::::;'.cc.....................lNWWXkodxOOO000Okdool;,''';::;...........
................',''',,'',;:cokOOO00000x,.......................'''',',,,';clccc::;'..;l,....................,ddc;.'dO0OOOOO00xl:,'',,'.'cl,..........
...............,c:,,'','',,,,;:cldddxxdc,.............................'''',,:odkkkl'..'l;..........................,dO0OOOOkdo:,',,'''',cl;......... .
..............':lc;',','',,,,;,,,,;;;;;,,'.............................''',,,:odkK0c...:c...........................'oxdol:;;,'','','';lc,............
..............;lllc,','',,;,,,,,,,;;,,,;'..............................,'',,,,;ldx0O:..;l'..........................',,,,,,;,,''''',,clc'.............
..............:llllc;''',;;,,;,,,,,,,,,'. ............................',,,,,,;,;codOx;.;l,..........................',;,;;,,,,'''',:cllc:,............
..............':lllll:,,,,,,,,;,,,,,,,,.     .........................'',,,,,;;,,;coxd,'l;...........................,,,,,,''',,;clll;.':c:;'.........
................:llllllllllcccccc::::;;'..    .......................''',,,,,,;;,,,;cdc,cc........................   .,'',;:ccodddl;'.  ..,:c:,.......
....       ......;llllodxxxxxxxxxxxddddddollccc,.....................',,,;;;,,,;,,,,,;llol'.....................    .':clodxdoc:;,'..     ...,:c:,..
................,clc:;codddddddxxxxxxxxxxxxxxo;.....................''',,;;,,,,,,;,',,':odo;......................,codxdolc;,.........     .. ..,:c;..
..............,:c:'....'''',,,,;;;;:::ccccclc,......................',,,,,,,,,,,,;,',,,',:do;.................'lddddlc;,'..............         ...''.
.    .......':c;... ...............................................'',,,,,,,,,,,,;;,',,,',;od;..................,;,'.....................       .   ..
       ...';:;..    ...............................................',',,,,,,,,,,,,;,,,,,,,';oo;............................................         ..
 .  .  .';:;.      ................................................'',,,,,,,,,,,,,,,,,,,','',lo;.............................................
 .    .:l;.  ..    ................................................',,,,,,,,,,,,,;,,,,,,'','':do,..............................................  .....
 */
