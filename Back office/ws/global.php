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
require_once('../upgrade.php');
require_once('../config.php');

$datasets = Dataset::getAll();

if (!isset($_POST['timestamp'])) {
    Exec::e('KO');
}

$global = array(
    'medals' => array(
        (object) array(
            'id' => 1,
            'description' => 'Voici la description de la premiere medaille!',
            'id_obj' => 1,
            'nb' => 10,
            'points' => 15,
            'name' => 'Mousse',
            'url_picto' => 'https://encrypted-tbn1.google.com/images?q=tbn:ANd9GcR15vjCAJ1B7-VLcHo-_TA7YA2oiHCQtK7NKOuJgdBb23pqJhufJA'
        ),
        (object) array(
            'id' => 2,
            'description' => 'Voici la description de la deuxieme medaille!',
            'id_obj' => 1,
            'nb' => 20,
            'points' => 30,
            'name' => 'Marin',
            'url_picto' => 'https://encrypted-tbn0.google.com/images?q=tbn:ANd9GcQgH518iI__OpvVWPMhFt1_yPiygDGYlV7HWJiVKfpnty5VwIH-'
        ),
        (object) array(
            'id' => 3,
            'description' => 'Voici la description de la troisieme medaille!',
            'id_obj' => 1,
            'nb' => 30,
            'points' => 60,
            'name' => 'Capitaine',
            'url_picto' => 'https://encrypted-tbn3.google.com/images?q=tbn:ANd9GcSuCrh_Ck5cQq57R8OnIVPBx0nL7GG2WUWHes-Qcp8Z1MIBSSMXLA'
        )
    ),
    'challenges' => array(
        (object) array(
            'id' => 1,
            'description' => 'Voici le premiere challenge !',
            'id_obj' => 1,
            'nb' => 10,
            'points' => 15,
            'name' => 'Challenge de la mort 1',
            'time_end' => 1348090241
        ),
        (object) array(
            'id' => 2,
            'description' => 'Voici le deuxieme challenge !',
            'id_obj' => 1,
            'nb' => 20,
            'points' => 30,
            'name' => 'Challenge de la mort 2',
            'time_end' => 1348080241
        ),
        (object) array(
            'id' => 3,
            'description' => 'Voici le troisieme challenge !',
            'id_obj' => 1,
            'nb' => 30,
            'points' => 60,
            'name' => 'Challenge de la mort 3',
            'time_end' => 1348070241
        )
    ),
    'grades' => (object) array(
        'best' => array(
            (object) array(
                'name' => 'Toto',
                'grade' => 1,
                'nb_points' => 45664
            ),
            (object) array(
                'name' => "Tutu",
                'grade' => 2,
                'nb_points' => 44970
            ),
            (object) array(
                'name' => "Tata",
                'grade' => 3,
                'nb_points' => 40211
            )
        ),
        'current' => array(
            (object) array(
                'name' => "Titi",
                'grade' => 42,
                'nb_points' => 3001
            ),
            (object) array(
                'name' => "Toitoi",
                'grade' => 45,
                'nb_points' => 2566
            ),
            (object) array(
                'name' => "Tuut",
                'grade' => 48,
                'nb_points' => 2200
            ),
            (object) array(
                'name' => "Tont",
                'grade' => 49,
                'nb_points' => 2156
            ),
            (object) array(
                'name' => "Toutou",
                'grade' => 52,
                'nb_points' => 1765
            )
        )
    )
);

$liste_datasets = array();
$liste_rules = array();
$liste_points = array();

foreach ($datasets as $dataset) {

    $liste_datasets[] = (object) array(
                'delete' => false,
                'url_picto' => stripslashes(SITE_URL . '/tmd/content/' . 'picto' . $dataset->getID() . '.png'),
                'url_img' => stripslashes(SITE_URL . '/tmd/content/' . $dataset->getID() . '.png'),
                'name' => $dataset->getName(true),
                'id' => $dataset->getID(),
                'description' => $dataset->getDesc(true)
    );

    $parsor = Parsor::getFromID($dataset->getID());

    foreach ($parsor->getRules() as $rule) {
        $liste_rules[] = (object) array(
                    'delete' => false,
                    'name' => $rule->getName(),
                    'id' => $rule->getID(),
                    'description' => $rule->getDesc(),
                    'format' => $rule->getType(),
                    'user_defined' => true,
                    'metadata' => $rule->getData()
        );
    }


    $points = Point::getFromParsor($dataset->getID(), intval($_POST['timestamp']));
    foreach ($points as $point) {
        $data = array();
        foreach ($parsor->getRules() as $rule) {
            $metadata = array();
            $choices = $point->getAData($rule->getID());
            if ($rule->getType() == Field::$FREE) {
                $metadata = array('val' => $point->getAData($rule->getID()));
            } else if ($rule->getType() == Field::$RADIO) {
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
                    'url_img' => stripslashes(SITE_URL . '/tmd/content/' . $dataset->getID() . '.png'),
                    'spec' => $data
        );
    }
}

$global['types_points'] = $liste_datasets;
$global['types_spec'] = $liste_rules;
$global['points'] = $liste_points;

if (!empty($_POST['id'])) {
    $user = User::getFromID(intval($_POST['id']));
    $global['user'] = $user->getObject();
}

echo stripcslashes(json_encode((object) $global));
