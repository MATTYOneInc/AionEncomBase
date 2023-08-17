/*
 * This file is part of Encom.
 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package ai;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AttackIntention;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.ai2.handler.*;
import com.aionemu.gameserver.ai2.manager.SkillAttackManager;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skill.NpcSkillEntry;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

import java.util.concurrent.atomic.AtomicBoolean;

@AIName("aggressive")
public class AggressiveNpcAI2 extends GeneralNpcAI2
{
	private AtomicBoolean isStartEvent = new AtomicBoolean(false);
	
	@Override
	protected void handleDied() {
		DiedEventHandler.onDie(this);
	}
	
	@Override
	protected void handleDespawned() {
		super.handleDespawned();
	}
	
	@Override
	protected void handleAttack(Creature creature) {
		AttackEventHandler.onAttack(this, creature);
		if (Rnd.get(1, 100) < 10 && !getOwner().isInInstance()) {
			checkPercentage(getLifeStats().getHpPercentage());
		}
	}
	
	@Override
    protected void handleCreatureSee(Creature creature) {
        CreatureEventHandler.onCreatureSee(this, creature);
    }
	
    @Override
    protected void handleCreatureMoved(Creature creature) {
        CreatureEventHandler.onCreatureMoved(this, creature);
    }
	
    @Override
	protected void handleCreatureAggro(Creature creature) {
		AggroEventHandler.onAggro(this, creature);
	}
	
	@Override
	protected void handleFinishAttack() {
		AttackEventHandler.onFinishAttack(this);
	}
	
	@Override
	protected void handleAttackComplete() {
		AttackEventHandler.onAttackComplete(this);
	}
	
	@Override
    protected void handleTargetGiveup() {
        TargetEventHandler.onTargetGiveup(this);
    }
	
    @Override
    protected void handleTargetChanged(Creature creature) {
        TargetEventHandler.onTargetChange(this, creature);
    }
	
	@Override
	protected boolean handleGuardAgainstAttacker(Creature attacker) {
		return AggroEventHandler.onGuardAgainstAttacker(this, attacker);
	}
	
	@Override
	protected boolean handleCreatureNeedsSupport(Creature creature) {
		return AggroEventHandler.onCreatureNeedsSupport(this, creature);
	}
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
		    case 243204:
			case 243205:
			case 243206:
			case 243207:
			case 243216:
			case 243217:
			case 243218:
			case 243219:
			case 243213:
			case 243214:
			case 243215:
			case 243220:
			case 243221:
			case 243222:
			case 243223:
			case 243209:
			case 243210:
			case 243211:
			case 242828:
			case 242829:
			case 242830:
			case 242831:
			case 242836:
			case 242837:
			case 242838:
			case 242839:
			case 242832:
			case 242833:
			case 242834:
			case 242835:
			case 242824:
			case 242825:
			case 242826:
			case 242827:
			case 243247:
			case 243084:
			case 243085:
			case 243086:
			case 243087:
			case 243068:
			case 243069:
			case 243070:
			case 243071:
			case 243076:
			case 243077:
			case 243078:
			case 243079:
			case 243116:
			case 243117:
			case 243118:
			case 243119:
			case 243092:
			case 243093:
			case 243094:
			case 243095:
			case 242920:
			case 242921:
			case 242922:
			case 242923:
			case 242904:
			case 242905:
			case 242906:
			case 242907:
			case 242916:
			case 242917:
			case 242918:
			case 242919:
			case 242912:
			case 242913:
			case 242914:
			case 242915:
			case 242908:
			case 242909:
			case 242910:
			case 242911:
			case 242944:
			case 242945:
			case 242946:
			case 242947:
			case 242956:
			case 242957:
			case 242958:
			case 242959:
			case 242960:
			case 242961:
			case 242962:
			case 242963:
			case 242948:
			case 242949:
			case 242950:
			case 242951:
			case 242240:
			case 242228:
			case 242232:
			case 242236:
			case 242224:
			case 242265:
			case 242273:
			case 242276:
			case 242280:
			case 242268:
			case 242197:
			case 242192:
			case 242193:
			case 242194:
			case 242195:
			case 238268:
			case 238269:
			case 238270:
			case 238271:
			case 238293:
			case 238294:
			case 238295:
			case 238292:
			case 238286:
			case 238287:
			case 238288:
			case 238289:
			case 238226:
			case 238227:
			case 238228:
			case 238229:
			case 238244:
			case 238245:
			case 238246:
			case 238247:
			case 238249:
			case 238250:
			case 238251:
			case 238252:
			case 238255:
			case 238256:
			case 238257:
			case 238254:
			case 238508:
			case 238509:
			case 238510:
			case 238511:
			case 238518:
			case 238519:
			case 238520:
			case 238521:
			case 238513:
			case 238514:
			case 238515:
			case 238516:
			case 238498:
			case 238499:
			case 238500:
			case 238501:
			case 241950:
			case 241951:
			case 241960:
			case 241961:
			case 241962:
			case 241963:
			case 241956:
			case 241957:
			case 241958:
			case 241959:
			case 241954:
			case 241955:
			case 241944:
			case 241945:
			case 241946:
			case 241947:
			case 241824:
			case 241825:
			case 241826:
			case 241827:
			case 241832:
			case 241833:
			case 241834:
			case 241835:
			case 241828:
			case 241829:
			case 241830:
			case 241831:
			case 241840:
			case 241841:
			case 241842:
			case 241843:
			case 242036:
			case 242037:
			case 242038:
			case 242039:
			case 242024:
			case 242025:
			case 242026:
			case 242027:
			////////////
			case 220308:
			case 220309:
			case 220310:
			case 220311:
			case 220312:
			case 220313:
			case 220334:
			case 220335:
			////////////
			case 243299:
			case 243300:
			case 243301:
			case 243302:
			case 243303:
			case 243304:
			case 243305:
			case 243314:
			case 243315:
			case 243318:
			case 243319:
			case 243320:
			case 243321:
			case 243322:
			case 243347:
			case 243348:
			case 243349:
			case 243350:
			case 243351:
			case 243352:
			case 243362:
			case 243363:
			case 243365:
			case 243366:
			case 243367:
			case 243368:
			case 243369:
			////////////
			case 220458:
			case 220465:
			case 220475:
			case 220498:
			case 220504:
			case 220673:
			////////////
			case 246405:
			////////////
			case 247063:
		        typeB();
			break;
		} switch (getNpcId()) {
		    case 243244:
			case 243245:
			case 243246:
			case 243247:
			case 243256:
			case 243257:
			case 243258:
			case 243259:
			case 243252:
			case 243253:
			case 243254:
			case 243255:
			case 243248:
			case 243249:
			case 243250:
			case 243241:
			case 243260:
			case 243261:
			case 243262:
			case 243263:
			case 239084:
			case 239085:
			case 239086:
			case 239087:
			case 239088:
			case 239089:
			case 240797:
			case 240798:
			case 240799:
			case 240800:
			case 240801:
			case 240802:
			case 243108:
			case 243109:
			case 243110:
			case 243111:
			case 243104:
			case 243105:
			case 243106:
			case 243107:
			case 242992:
			case 242993:
			case 242994:
			case 242995:
			case 242852:
			case 242853:
			case 242854:
			case 242855:
			case 242996:
			case 242997:
			case 242998:
			case 242999:
			case 242848:
			case 242849:
			case 242850:
			case 242851:
			case 242860:
			case 242861:
			case 242862:
			case 242863:
			case 242856:
			case 242857:
			case 242858:
			case 242859:
			case 242576:
			case 242577:
			case 242578:
			case 242579:
			case 242988:
			case 242989:
			case 242990:
			case 242991:
			case 242244:
			case 241912:
			case 241913:
			case 241914:
			case 241915:
			case 241920:
			case 241921:
			case 241922:
			case 241923:
			case 241916:
			case 241917:
			case 241918:
			case 241919:
			case 242060:
			case 242061:
			case 242062:
			case 242063:
			case 242044:
			case 242045:
			case 242046:
			case 242047:
			case 242052:
			case 242053:
			case 242054:
			case 242055:
			case 242056:
			case 242057:
			case 242058:
			case 242059:
			////////////
			case 220314:
			case 220315:
			case 220316:
			case 220329:
			case 220330:
			case 220331:
			case 220332:
			case 220333:
			////////////
			case 243310:
			case 243311:
			case 243312:
			case 243313:
			case 243316:
			case 243317:
			case 243330:
			case 243331:
			case 243332:
			case 243333:
			case 243334:
			case 243335:
			case 243336:
			case 243337:
			case 243353:
			case 243354:
			case 243355:
			case 243356:
			case 243357:
			case 243364:
			////////////
			case 220480:
			case 220487:
			case 220488:
			case 220491:
			case 220492:
			case 220494:
			////////////
			case 246394:
			case 246398:
			case 246402:
			case 246406:
			case 246410:
			case 246441:
			case 246940:
			////////////
			case 247064:
		        typeC();
			break;
		} switch (getNpcId()) {
			case 243056:
			case 243057:
			case 243058:
			case 243059:
			case 243012:
			case 243013:
			case 243014:
			case 243015:
			case 243020:
			case 243021:
			case 243022:
			case 243023:
			case 242700:
			case 242701:
			case 242702:
			case 242703:
			case 242712:
			case 242713:
			case 242714:
			case 242715:
			case 242704:
			case 242705:
			case 242706:
			case 242707:
			case 242716:
			case 242717:
			case 242718:
			case 242719:
			case 242720:
			case 242721:
			case 242722:
			case 242723:
			case 242708:
			case 242709:
			case 242710:
			case 242711:
			case 242600:
			case 242601:
			case 242602:
			case 242603:
			case 242592:
			case 242593:
			case 242594:
			case 242595:
			case 242672:
			case 242673:
			case 242674:
			case 242675:
			case 242664:
			case 242665:
			case 242666:
			case 242667:
			case 242676:
			case 242677:
			case 242678:
			case 242679:
			case 242552:
			case 242553:
			case 242554:
			case 242555:
			case 242556:
			case 242557:
			case 242558:
			case 242559:
			case 242548:
			case 242549:
			case 242550:
			case 242551:
			case 243052:
			case 243053:
			case 243054:
			case 243055:
			case 243044:
			case 243045:
			case 243046:
			case 243047:
			case 243048:
			case 243049:
			case 243050:
			case 243051:
			case 242872:
			case 242873:
			case 242874:
			case 242875:
			case 242876:
			case 242877:
			case 242878:
			case 242879:
			case 242880:
			case 242881:
			case 242882:
			case 242883:
			case 242864:
			case 242865:
			case 242866:
			case 242867:
			case 242868:
			case 242869:
			case 242870:
			case 242871:
			case 238220:
			case 238221:
			case 238222:
			case 238223:
			case 238196:
			case 238197:
			case 238198:
			case 238199:
			case 238200:
			case 238201:
			case 238202:
			case 238203:
			case 238206:
			case 238207:
			case 238208:
			case 238209:
			case 238212:
			case 238213:
			case 238214:
			case 238215:
			case 241980:
			case 241981:
			case 241982:
			case 241983:
			case 241972:
			case 241973:
			case 241974:
			case 241975:
			case 241976:
			case 241977:
			case 241978:
			case 241979:
			case 241964:
			case 241965:
			case 241966:
			case 241967:
			case 241968:
			case 241969:
			case 241970:
			case 241971:
			case 241852:
			case 241853:
			case 241854:
			case 241855:
			case 241844:
			case 241845:
			case 241846:
			case 241847:
			case 241856:
			case 241857:
			case 241858:
			case 241859:
			case 241860:
			case 241861:
			case 241862:
			case 241863:
			////////////
			case 220317:
			case 220318:
			case 220319:
			case 220326:
			case 220327:
			case 220328:
			////////////
			case 243293:
			case 243294:
			case 243295:
			case 243296:
			case 243297:
			case 243298:
			case 243323:
			case 243324:
			case 243325:
			case 243326:
			case 243327:
			case 243328:
			case 243329:
			case 243340:
			case 243341:
			case 243342:
			case 243343:
			case 243344:
			case 243345:
			case 243346:
			case 243370:
			case 243371:
			case 243372:
			case 243373:
			case 243374:
			case 243375:
			////////////
			case 220459:
			case 220462:
			case 220466:
			case 220469:
			case 220476:
			case 220479:
			case 220483:
			case 220484:
			case 220499:
			case 220500:
			case 220503:
			case 220597:
			case 220674:
			////////////
			case 246403:
			case 246407:
		        typeD();
			break;
		} switch (getNpcId()) {
			case 243032:
			case 243033:
			case 243034:
			case 243035:
			case 243036:
			case 243037:
			case 243038:
			case 243039:
			case 243028:
			case 243029:
			case 243030:
			case 243031:
			case 243041:
			case 243042:
			case 243043:
			case 243044:
			case 243024:
			case 243025:
			case 243026:
			case 243027:
			case 243232:
			case 243233:
			case 243234:
			case 243235:
			case 243240:
			case 243241:
			case 243242:
			case 243243:
			case 243236:
			case 243237:
			case 243238:
			case 243239:
			case 243224:
			case 243225:
			case 243226:
			case 243227:
			case 243228:
			case 243229:
			case 243230:
			case 243231:
			case 242560:
			case 242561:
			case 242562:
			case 242563:
			case 242648:
			case 242649:
			case 242650:
			case 242651:
			case 242820:
			case 242821:
			case 242822:
			case 242823:
			case 242812:
			case 242813:
			case 242814:
			case 242815:
			case 242896:
			case 242897:
			case 242898:
			case 242899:
			case 242884:
			case 242885:
			case 242886:
			case 242887:
			case 242892:
			case 242893:
			case 242894:
			case 242895:
			case 242900:
			case 242901:
			case 242902:
			case 242903:
			case 242940:
			case 242941:
			case 242942:
			case 242943:
			case 242928:
			case 242929:
			case 242930:
			case 242931:
			case 242932:
			case 242933:
			case 242934:
			case 242935:
			case 242924:
			case 242925:
			case 242926:
			case 242927:
			case 242936:
			case 242937:
			case 242938:
			case 242939:
			case 241872:
			case 241873:
			case 241874:
			case 241875:
			case 241880:
			case 241881:
			case 241882:
			case 241883:
			case 241864:
			case 241865:
			case 241866:
			case 241867:
			case 241876:
			case 241877:
			case 241878:
			case 241879:
			case 242016:
			case 242017:
			case 242018:
			case 242019:
			case 242020:
			case 242021:
			case 242022:
			case 242023:
			case 242008:
			case 242009:
			case 242010:
			case 242011:
			////////////
			case 220305:
			case 220306:
			case 220307:
			case 220323:
			case 220324:
			case 220325:
			////////////
			case 243286:
			case 243287:
			case 243288:
			case 243289:
			case 243290:
			case 243291:
			case 243292:
			case 243306:
			case 243307:
			case 243308:
			case 243309:
			case 243338:
			case 243339:
			case 243358:
			case 243359:
			case 243360:
			case 243361:
			case 243376:
			case 243377:
			case 243378:
			case 243379:
			////////////
			case 220460:
			case 220467:
			case 220470:
			case 220471:
			case 220472:
			case 220477:
			case 220495:
			case 220594:
			case 220595:
			case 220670:
			case 220671:
			case 220672:
			case 220675:
			////////////
			case 246392:
			case 246396:
			case 246400:
			case 246404:
			case 246408:
			case 246416:
			case 246418:
			////////////
			case 247059:
		        typeA();
			break;
		} switch (getNpcId()) {
		    case 211408:
			case 211409:
			case 211410:
			case 211436:
			case 211437:
			case 211438:
			case 211457:
			case 211458:
			case 211459:
			case 211460:
			case 212427:
			case 212669:
			case 212672:
			case 212673:
			case 212811:
			case 212993:
		        iceClawBlessing();
			break;
		} switch (getNpcId()) {
			case 211573:
			case 211574:
			case 211575:
			case 211576:
			case 211577:
			case 211595:
			case 211598:
			case 211611:
			case 211612:
			case 212555:
			case 212724:
			case 212725:
			case 212729:
			case 212730:
			case 212731:
			case 212732:
			case 212736:
			case 212738:
			case 212740:
			case 212741:
			case 212743:
			case 212744:
			case 212809:
			case 212865:
			case 212866:
			case 212867:
			case 212981:
			case 212982:
		        mistShadeBlessing();
			break;
		} switch (getNpcId()) {
		    case 214827:
			case 214828:
			case 214829:
			case 214831:
			case 214832: 
			case 214834:
			case 214836:
			case 214837:
			case 214838:
			case 214841:
			case 214842:
			case 214844:
			case 214845:
			case 214848:
			case 214852:
			case 214853:
			case 214854:
			case 214856:
			case 214857:
			case 214858:
			case 214865:
			case 214866:
			case 214872:
			case 214873:
			case 214881:
			case 214882:
			case 214883:
			case 214890:
			case 214892:
			case 215223:
			case 215224:
			case 215226:
			case 215227:
			case 215229:
			case 215230:
			case 215232:
			case 215233:
			case 215234:
			case 215235:
			case 215236:
			case 215237:
			case 215244:
			case 215245:
			case 215246:
			case 215247:
			case 215248:
			case 215249:
			case 215253:
			case 215254:
			case 215255:
			case 215256:
			case 215257:
			case 215258:
			case 215261:
			case 215262:
			case 215263:
			case 215264:
			case 215265:
			case 215266:
			case 215267:
			case 215271:
			case 215272:
			case 215273:
			case 215274:
			case 215275:
			case 215276:
			case 215428:
			case 215429:
			case 215430:
			case 215452:
		        anuhartBravery();
			break;
		} switch (getNpcId()) {
		    case 219829:
			case 219830:
			case 219831:
			case 219832:
			case 219833:
			case 219834:
			case 219835:
			case 219836:
			case 219837:
			case 219838:
			case 219839:
			case 219840:
			case 219841:
			case 219842:
			case 219843:
			case 219844:
			case 219845:
			case 219846:
			case 219847:
			case 219848:
			case 219849:
			case 219850:
			case 219851:
			case 219940:
			case 219941:
			case 219942:
			case 219943:
			case 219944:
			case 219945:
			case 219946:
			case 219947:
			case 219948:
			case 219949:
			case 219952:
			case 219953:
			case 220023:
			case 220024:
			case 220033:
			case 220034:
			case 220039:
			case 220040:
			case 220041:
			case 220063:
			case 220064:
			case 220065:
			case 220066:
			case 220067:
			case 220101:
			case 220102:
			case 220103:
			case 220104:
			case 220105:
			case 220106:
			case 220149:
			case 220150:
			case 220188:
			case 220189:
			case 233910:
            case 233911:
			case 235921:
			case 235922:
			case 235923:
			case 235924:
			case 235925:
			case 235926:
			case 235927:
			case 235928:
			case 235929:
			case 235930:
			case 235931:
			case 235932:
			case 235933:
			case 235934:
			case 235935:
			case 235936:
			case 235937:
			case 235938:
			case 236252:
			case 236253:
			case 236254:
			case 236255:
			case 236256:
			case 236257:
			case 236258:
			case 236264:
            case 236265:
			case 236701:
			case 236702:
			case 236703:
		        survivalInstinct();
			break;
		} switch (getNpcId()) {
			case 209664:
			case 209669:
			case 209670:
			case 209675:
			case 219868:
            case 219875:
            case 219876:
            case 219881:
            case 219882:
            case 219888:
            case 219889:
            case 219894:
            case 219895:
            case 219900:
            case 219901:
            case 219907:
            case 219908:
            case 219914:
            case 219915:
            case 219920:
            case 219921:
            case 220073:
            case 220074:
            case 220077:
            case 220078:
            case 220081:
            case 220082:
            case 220085:
            case 220086:
            case 220089:
            case 220090:
            case 220093:
            case 220094:
            case 220097:
            case 220098:
            case 220163:
            case 220166:
            case 220169:
            case 220172:
			case 234033:
			case 234034:
			case 234039:
			case 234040:
			case 234075:
			case 234076:
			case 234078:
			case 234079:
			case 234090:
			case 234091:
			case 234093:
			case 234094:
			case 234757:
			case 234758:
			case 234760:
			case 234761:
			case 234775:
			case 234776:
			case 234778:
			case 234779:
			case 234793:
			case 234794:
			case 234796:
			case 234797:
			case 234811:
			case 234812:
			case 234814:
			case 234815:
			case 234823:
			case 234824:
			case 234826:
			case 234827:
			case 234835:
			case 234836:
			case 234838:
			case 234839:
			case 234847:
			case 234848:
			case 234850:
			case 234851:
			case 234859:
			case 234860:
			case 234862:
			case 234863:
			case 234871:
			case 234872:
			case 234874:
			case 234875:
			case 234883:
			case 234884:
			case 234886:
			case 234887:
			case 234895:
			case 234896:
			case 234898:
			case 234899:
			case 234465:
			case 234918:
			case 234919:
			case 234920:
			case 234921:
			case 234954:
			case 234955:
			case 234956:
			case 234957:
			case 236023:
			case 236024:
			case 236029:
			case 236030:
			case 236036:
			case 236037:
			case 236042:
			case 236043:
			case 236049:
			case 236050:
			case 236056:
			case 236057:
			case 236062:
			case 236063:
			case 236068:
			case 236069:
			case 236673:
			case 236674:
			case 236677:
			case 236678:
			case 236681:
			case 236682:
			case 236685:
			case 236686:
			case 236689:
			case 236690:
			case 236693:
			case 236694:
			case 236697:
			case 236698:
			case 236704:
			case 256698:
			case 256699:
			case 256710:
			case 256711:
			case 256722:
			case 256723:
			case 256734:
			case 256735:
			case 257015:
			case 257020:
			case 257117:
			case 257118:
			case 257162:
			case 257163:
			case 257315:
			case 257320:
			case 257417:
			case 257418:
			case 257462:
			case 257463:
			case 257615:
			case 257620:
			case 257717:
			case 257718:
			case 257762:
			case 257763:
			case 257915:
			case 257920:
			case 258017:
			case 258018:
			case 258062:
			case 258063:
			case 263016:
			case 263021:
			case 263316:
			case 263321:
			case 263616:
			case 263621:
			case 264516:
			case 264521:
			case 264816:
			case 264821:
			case 265116:
			case 265121:
			case 265416:
			case 265421:
			case 265716:
			case 265721:
			case 266016:
			case 266021:
			case 279070:
			case 279134:
			case 279136:
			case 279144:
			case 279162:
			case 279176:
			case 279232:
			case 279241:
			case 279242:
			case 279246:
			case 279266:
			case 279274:
			case 279330:
			case 279334:
			case 279340:
			case 279344:
			case 279364:
			case 279428:
			case 279430:
			case 279438:
			case 279456:
			case 279470:
			case 279526:
			case 279535:
			case 279536:
			case 279540:
			case 279560:
			case 279568:
			case 279624:
			case 279628:
			case 279634:
			case 279638:
			case 883076:
			case 883077:
			case 883082:
			case 883083:
			case 883088:
			case 883089:
			case 883094:
			case 883095:
			case 883100:
			case 883101:
			case 883106:
			case 883107:
			case 883112:
			case 883113:
			case 883118:
			case 883119:
			case 883124:
			case 883125:
			case 883130:
			case 883131:
			case 883136:
			case 883137:
			case 883142:
			case 883143:
			case 883148:
			case 883149:
			case 883154:
			case 883155:
			case 883160:
			case 883161:
			case 883166:
			case 883167:
			case 883172:
			case 883173:
			case 883178:
			case 883179:
			case 883184:
			case 883185:
			case 883190:
			case 883191:
			case 883196:
			case 883197:
			case 883202:
			case 883203:
			case 883208:
			case 883209:
			case 883214:
			case 883215:
			case 883220:
			case 883221:
			case 883226:
			case 883227:
			case 883232:
			case 883233:
			case 883238:
			case 883239:
			case 883244:
			case 883245:
			case 883250:
			case 883251:
			case 883256:
			case 883257:
			case 883262:
			case 883263:
				conquerorPassion();
			break;
		} switch (getNpcId()) {
		    case 230786:
			case 230787:
			case 230791:
			case 230810:
			case 230814:
			case 230815:
			case 230818:
			case 230820:
			case 233333:
			case 233339:
			case 233431:
			case 233436:
			case 235576:
			case 235578:
			case 235582:
			case 235583:
			case 235601:
			case 235602:
			case 235604:
			case 235608:
			case 235609:
			    midnightRobe();
			break;
		} switch (getNpcId()) {
			case 233138:
			case 235566:
			case 235567:
			case 235569:
			case 235570:
			case 235571:
			case 235574:
			case 235588:
			case 235589:
			case 235592:
			case 235593:
			case 235595:
			case 235596:
			case 235597:
			case 235600:
			case 235614:
			case 235615:
			    firmBelief();
			break;
		} switch (getNpcId()) {
		    case 230853:
			case 230857:
			case 233255:
			case 233256:
			case 233257:
			    beritraFavor();
			break;
		} switch (getNpcId()) {
			case 219926:
			case 219927:
			case 219928:
			case 219929:
			case 219930:
			case 219931:
			case 219932:
			case 219933:
			case 219934:
			case 220020:
			case 235965:
			case 235967:
			case 235968:
			case 235969:
			case 235970:
			case 235971:
			case 235972:
		        darkLordBlessing();
			break;
		} switch (getNpcId()) {
			case 882456:
			case 882457:
			case 882621:
			case 882622:
			case 882623:
			case 882624:
			case 882786:
			case 882787:
			case 882788:
			case 882789:
			case 882981:
			case 882987:
			case 882993:
			case 882999:
			case 883005:
			case 883011:
			case 883052:
			case 883053:
			case 883058:
			case 883059:
			case 883064:
			case 883065:
			case 883070:
			case 883071:
			////////////
			case 246556:
			case 246557:
			case 246559:
			case 246561:
			case 246563:
			case 246564:
			case 246565:
			case 246566:
			case 246568:
			case 246569:
			case 246570:
			case 246581:
			case 246582:
			case 246584:
			case 246585:
			case 246727:
			case 246728:
			case 246730:
			case 246797:
			case 246798:
			case 246850:
			case 246851:
			case 246853:		
			case 246854:
			case 246855:
			case 246856:
			case 246857:
			case 246858:
			case 246863:
			case 246864:
			case 246865:
			case 246868:
			case 246869:
			case 246870:
			case 246871:
			case 246874:
			case 246881:
			case 246885:
			case 246895:
			case 246905:
			case 246926:
			case 246928:
			case 246930:
			case 246931:
			case 246932:
			case 246941:
			case 246942:
			case 246943:
			case 246944:
			case 246945:
			case 246946:
			case 246947:
			case 246948:
			case 246991:
			case 246993:
			case 247113:
			case 247114:
			case 247115:
			case 247116:
			case 247117:
			case 247123:
			case 247124:
			case 247132:
			case 247133:
			case 247181:
			case 247208:
			case 247209:
				ereshkigalRage();
			break;
		} switch (getNpcId()) {
		    case 237108:
			case 237110:
			case 237248:
			case 237249:
			//5.0
			case 220424:
			    elementalLordship();
		    break;
		} switch (getNpcId()) {
		    case 233302:
			case 233303:
			case 233304:
			case 233305:
			    malevolence();
		    break;
		} switch (getNpcId()) {
		    case 231130:
			    exultation();
		    break;
		} switch (getNpcId()) {
		    case 236204:
			case 236205:
			case 236206:
			case 236216:
			case 236217:
			case 236218:
			case 236219:
			case 236220:
			case 236235:
			case 236236:
			case 236237:
			case 236238:
			    orderPerfectObeisance();
		    break;
		} switch (getNpcId()) {
			case 234032:
			case 234035:
			case 234038:
			case 234041:
			case 234074:
			case 234077:
			case 234080:
			case 234089:
			case 234092:
			case 234756:
			case 234759:
			case 234774:
			case 234777:
			case 234792:
			case 234795:
			case 234810:
			case 234813:
			case 234822:
			case 234825:
			case 234834:
			case 234837:
			case 234846:
			case 234849:
			case 234858:
			case 234861:
			case 234870:
			case 234873:
			case 234882:
			case 234885:
			case 234894:
			case 234897:
			case 235172:
			case 235173:
			case 883016:
			case 883017:
			case 883022:
			case 883023:
			case 883028:
			case 883029:
			case 883034:
			case 883035:
			case 883040:
			case 883041:
			case 883046:
			case 883047:
				brokenMorale();
			break;
		} switch (getNpcId()) {
			case 857785:
			case 857792:
			case 857796:
			case 857800:
				knowledgeOfFlame();
			break;
		} switch (getNpcId()) {
			case 857786:
			case 857793:
			case 857797:
			case 857801:
				knowledgeOfWater();
			break;
		} switch (getNpcId()) {
			case 857787:
			case 857794:
			case 857798:
			case 857802:
				knowledgeOfEarth();
			break;
		} switch (getNpcId()) {
			case 857788:
			case 857795:
			case 857799:
			case 857803:
				knowledgeOfAir();
			break;
		} switch (getNpcId()) {
			case 246388:
			case 246389:
			case 246390:
			case 246391:
			case 246412:
			case 246413:
			case 246414:
			case 246415:
			case 246425:
			case 246426:
			case 246431:
			case 246432:
			case 246715:
			case 246716:
			case 246717:
			case 246719:
			case 246720:
			case 246721:
			case 246738:
			case 246740:
			case 246742:
				IDEternity03Guard();
			break;
		}
	}
	
	private void typeA() {
	    SkillEngine.getInstance().getSkill(getOwner(), 22987, 1, getOwner()).useNoAnimationSkill(); //Warrior Type.
	}
	private void typeB() {
	    SkillEngine.getInstance().getSkill(getOwner(), 22988, 1, getOwner()).useNoAnimationSkill(); //Assassin Type.
	}
	private void typeC() {
	    SkillEngine.getInstance().getSkill(getOwner(), 22989, 1, getOwner()).useNoAnimationSkill(); //Mage Type.
	}
	private void typeD() {
	    SkillEngine.getInstance().getSkill(getOwner(), 22990, 1, getOwner()).useNoAnimationSkill(); //Special Type.
	}
	private void iceClawBlessing() {
	    SkillEngine.getInstance().getSkill(getOwner(), 16979, 1, getOwner()).useNoAnimationSkill(); //Ice Claw Blessing.
	}
	private void mistShadeBlessing() {
	    SkillEngine.getInstance().getSkill(getOwner(), 16980, 1, getOwner()).useNoAnimationSkill(); //Mist Shade Blessing.
	}
	private void anuhartBravery() {
	    SkillEngine.getInstance().getSkill(getOwner(), 18168, 1, getOwner()).useNoAnimationSkill(); //Anuhart's Bravery.
	}
	private void survivalInstinct() {
	    SkillEngine.getInstance().getSkill(getOwner(), 20656, 1, getOwner()).useNoAnimationSkill(); //Survival Instinct.
	}
	private void conquerorPassion() {
		SkillEngine.getInstance().getSkill(getOwner(), 20665, 1, getOwner()).useNoAnimationSkill(); //Conqueror's Passion.
	}
	private void midnightRobe() {
		SkillEngine.getInstance().getSkill(getOwner(), 20700, 1, getOwner()).useNoAnimationSkill(); //Midnight Robe.
	}
	private void firmBelief() {
		SkillEngine.getInstance().getSkill(getOwner(), 20962, 1, getOwner()).useNoAnimationSkill(); //Firm Belief.
	}
	private void beritraFavor() {
	    SkillEngine.getInstance().getSkill(getOwner(), 21135, 1, getOwner()).useNoAnimationSkill(); //Beritra's Favor.
	}
	private void malevolence() {
	    SkillEngine.getInstance().getSkill(getOwner(), 21181, 1, getOwner()).useNoAnimationSkill(); //Malevolence.
	}
	private void exultation() {
	    SkillEngine.getInstance().getSkill(getOwner(), 21236, 1, getOwner()).useNoAnimationSkill(); //Exultation.
	}
	private void orderPerfectObeisance() {
	    SkillEngine.getInstance().getSkill(getOwner(), 21844, 1, getOwner()).useNoAnimationSkill(); //Order Perfect Obeisance.
	}
	private void darkLordBlessing() {
	    SkillEngine.getInstance().getSkill(getOwner(), 22664, 1, getOwner()).useNoAnimationSkill(); //Dark Lord's Blessing.
	}
	private void ereshkigalRage() {
	    SkillEngine.getInstance().getSkill(getOwner(), 22682, 1, getOwner()).useNoAnimationSkill(); //Ereshkigal Rage.
	}
	private void elementalLordship() {
	    SkillEngine.getInstance().getSkill(getOwner(), 22744, 1, getOwner()).useNoAnimationSkill(); //Elemental Lordship.
	}
	private void brokenMorale() {
		SkillEngine.getInstance().getSkill(getOwner(), 22791, 1, getOwner()).useNoAnimationSkill(); //Broken Morale.
	}
	private void knowledgeOfFlame() {
		SkillEngine.getInstance().getSkill(getOwner(), 22943, 1, getOwner()).useNoAnimationSkill(); //Knowledge Of Flame.
	}
	private void knowledgeOfEarth() {
		SkillEngine.getInstance().getSkill(getOwner(), 22944, 1, getOwner()).useNoAnimationSkill(); //Knowledge Of Earth.
	}
	private void knowledgeOfWater() {
		SkillEngine.getInstance().getSkill(getOwner(), 22945, 1, getOwner()).useNoAnimationSkill(); //Knowledge Of Water.
	}
	private void knowledgeOfAir() {
		SkillEngine.getInstance().getSkill(getOwner(), 22969, 1, getOwner()).useNoAnimationSkill(); //Knowledge Of Air.
	}
	private void IDEternity03Guard() {
		SkillEngine.getInstance().getSkill(getOwner(), 17753, 1, getOwner()).useNoAnimationSkill(); //IDEternity_03_Guard_Buff.
	}
	
	@Override
	protected AIAnswer pollInstance(AIQuestion question) {
		switch (question) {
	        case CAN_SPAWN_ON_DAYTIME_CHANGE:
			    return AIAnswers.POSITIVE;
			case SHOULD_DECAY:
			    return AIAnswers.POSITIVE;
			case SHOULD_RESPAWN:
			    return AIAnswers.POSITIVE;
			case SHOULD_REWARD:
			    return AIAnswers.POSITIVE;
			case SHOULD_REWARD_AP:
			    return AIAnswers.POSITIVE;
			case SHOULD_REWARD_GP:
			    return AIAnswers.POSITIVE;
			case CAN_RESIST_ABNORMAL:
			    return AIAnswers.POSITIVE;
			case CAN_ATTACK_PLAYER:
			    return AIAnswers.POSITIVE;
			default:
				return null;
		}
	}
	
	private void checkPercentage(int hpPercentage) {
		VisibleObject currentTarget = getTarget();
		if (hpPercentage <= 30) {
			if (isStartEvent.compareAndSet(false, true)) {
				SkillEngine.getInstance().applyEffectDirectly(17818, getOwner(), getOwner(), 5000);
				if (currentTarget instanceof Player) {
					//%0 is running away.
            	    PacketSendUtility.sendPacket((Player) currentTarget, SM_SYSTEM_MESSAGE.STR_UI_COMBAT_NPC_FLEE(getOwner().getName()));
            	}
			}
		}
	}
	
    @Override
	public AttackIntention chooseAttackIntention() {
		VisibleObject currentTarget = getTarget();
		Creature mostHated = getAggroList().getMostHated();
		if (mostHated == null || mostHated.getLifeStats().isAlreadyDead()) {
			return AttackIntention.FINISH_ATTACK;
		} if (currentTarget == null || !currentTarget.getObjectId().equals(mostHated.getObjectId())) {
			onCreatureEvent(AIEventType.TARGET_CHANGED, mostHated);
			return AttackIntention.SWITCH_TARGET;
		} if (getOwner().getObjectTemplate().getAttackRange() == 0) {
			NpcSkillEntry skill = getOwner().getSkillList().getRandomSkill();
			if (skill != null) {
				skillId = skill.getSkillId();
				skillLevel = skill.getSkillLevel();
				return AttackIntention.SKILL_ATTACK;
			}
		} else {
			NpcSkillEntry skill = SkillAttackManager.chooseNextSkill(this);
			if (skill != null) {
				skillId = skill.getSkillId();
				skillLevel = skill.getSkillLevel();
				return AttackIntention.SKILL_ATTACK;
			}
		}
		return AttackIntention.SIMPLE_ATTACK;
	}
}