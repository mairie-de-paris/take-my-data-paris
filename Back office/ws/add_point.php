<?php

/**
 * Copyright 2012-2013 Mairie de Paris
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *limitations under the License.
**/

define('EXEC', 0xA51D1E);

require_once('../config.php');

if (isset($_POST['obj'], $_POST['id'])) {
    $post = json_decode($_POST['obj']);
    $parsor = Parsor::getFromID(intval($post->id_type));
    $point = Point::getFromPos(intval($post->id_type), $post->lat, $post->long);

    $user = User::getFromID($_POST['id']);

    if (!$point) {
        $data = array();

        foreach ($parsor->getRules() as $rule) {
            $choices = $rule->getData();

            if (empty($choices)) {
                $data[$rule->getID()] = '';
            } else {
                $data[$rule->getID()] = array();
                foreach ($choices as $choice) {
                    $data[$rule->getID()][$choice] = 0;
                }
            }
        }
        $id_point = Point::create($post->id_type, $post->lat, $post->long, $data);
        $point = Point::getFromID($id_point);
    }
    if ($user) {
        if ($point && $user->addPoint($post->id_type, $point->getID())) {
            $data = $point->getData();

            foreach ($post->spec as $spec) {
                $ruleID = $spec->id_spec;
                $rule = $parsor->getRule($ruleID);
                $choices = $rule->getData();

                if (empty($choices) && empty($data[$ruleID])) { //TODO envoyer en moderation
                    $data[$ruleID] = $spec->metadata->val;
                } else {
                    foreach ($choices as $choice) {
                        foreach ($spec->metadata->ok as $ok) {
                            if ($ok === $choice) {
                                $data[$ruleID][$choice] += 1;
                                break;
                            }
                        }
                    }
                }
            }

            $point->plusOne($data);
            $user->update();
            Exec::e('OK');
        }
        Exec::e('ALREADY');
    }
    Exec::e('NO_USER');
}

Exec::e('KO');