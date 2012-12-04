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
 * limitations under the License.
**/

defined('EXEC') or die('Access forbidden');

$dataset = $this->current;

$liste_points = array();

$parsor = Parsor::getFromID($dataset->getID());
$points = Point::getFromParsor($dataset->getID());
foreach ($points as $point) {
    $data = array();
    foreach ($parsor->getRules() as $rule) {
        $metadata = array();
        $choices = $point->getAData($rule->getID());
        if ($rule->getType() == Field::$FREE) {
            $metadata = array('val' => $point->getAData($rule->getID()));
        } else if ($rule->getType() == Field::$CHECKBOX) {
            $best = null;
            foreach ($choices as $choice => $count) {
                if ($best === null) {
                    $best = array($choice, $count);
                    continue;
                } else {
                    if ($count > $best[1]) {
                        $best = array($choice, $count);
                    }
                }
            }
            $metadata['val'] = $best[0];
        } else {
            $metadata['ok'] = array();
            $metadata['ko'] = array();
            foreach ($choices as $choice => $count) {
                $percent = ($count / $point->getCount()) * 100.;
                if ($percent >= TRUST_LIMIT) {
                    $metadata['ok'][] = $choice;
                } else {
                    $metadata['ko'][] = $choice;
                }
            }
        }
        $data[] = (object) array(
                    'id_spec' => $rule->getID(),
                    'metadata' => (object) $metadata
        );
    }
    $liste_points[] = (object) array(
                'timestamp' => $point->getLastUpdate(),
                'id' => $point->getID(),
                'id_type' => $dataset->getID(),
                'lat' => floatval($point->getLat()),
                'long' => floatval($point->getLon()),
                'url_img' => stripslashes(SITE_URL . CONTENT_ROOT . $dataset->getID() . '.png'),
                'spec' => $data
    );
}

echo stripslashes(json_encode($liste_points));