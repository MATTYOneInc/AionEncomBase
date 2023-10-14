/*

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
package com.aionemu.gameserver.dataholders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.mail_reward.MailRewardTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Created by Wnkrz on 26/07/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "reward_mail_templates")
public class MailRewardData {

    @XmlElement(name = "reward_mail_template")
    private List<MailRewardTemplate> RewardMail;

    @XmlTransient
    private TIntObjectHashMap<MailRewardTemplate> templates = new TIntObjectHashMap<MailRewardTemplate>();

    @XmlTransient
    private Map<Integer, MailRewardTemplate> templatesMap = new HashMap<Integer, MailRewardTemplate>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (MailRewardTemplate template : RewardMail) {
            templates.put(template.getId(), template);
            templatesMap.put(template.getId(), template);
        }
        RewardMail.clear();
        RewardMail = null;
    }

    public int size() {
        return templates.size();
    }

    public MailRewardTemplate getMailReward(int rewardId) {
        return templates.get(rewardId);
    }

    public Map<Integer, MailRewardTemplate> getAll() {
        return templatesMap;
    }
}