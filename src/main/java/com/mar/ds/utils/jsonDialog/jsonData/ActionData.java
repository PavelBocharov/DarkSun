package com.mar.ds.utils.jsonDialog.jsonData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionData implements Serializable {

    private Long id;
    private Long next;
    private String title;
    private Long needItemId;
    private Long needMissionId;
    private Long needTaskId;
    private boolean moveMission;

    private Boolean isTeleport;
    private Boolean saveGame;
    private String level;

    private Float positionX;
    private Float positionY;
    private Float positionZ;

    private Float rotationX;
    private Float rotationY;
    private Float rotationZ;

}
