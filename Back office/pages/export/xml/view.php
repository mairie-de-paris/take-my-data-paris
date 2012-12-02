<?php

/**
 * Copyright 2012-2013 The Apache Software Foundation
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

$parsor = Parsor::getFromID($dataset->getID());
$points = Point::getFromParsor($dataset->getID());
echo '<points>';
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
    echo '<point>';
    echo '<update>' . $point->getLastUpdate() . '</update>';
    echo '<id>' . $point->getID() . '</id>';
    echo '<type_id>' . $dataset->getID() . '</type_id>';
    echo '<lat>' . floatval($point->getLat()) . '</lat>';
    echo '<lon>' . floatval($point->getLon()) . '</lon>';
    echo '<img>' . stripslashes(SITE_URL . CONTENT_ROOT . $dataset->getID() . '.png') . '</img>';
    echo '<spec>' . json_encode($data) . '</spec>';
    echo '</point>';
}
echo '</points>';