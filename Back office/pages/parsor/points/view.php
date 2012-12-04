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

?>
<div class="center">
    <?php
    $rules = $this->current->getRules();
    ?>
    <table border="1" style="width: 100%; border: 1px solid;">
        <thead>
            <tr>
                <td>#ID</td>
                <td>Latitude</td>
                <td>Longitude</td>
                <?php foreach ($rules as $rule) { ?>
                    <td><?= $rule->getName(); ?></td>
                <?php } ?>
            </tr>
        </thead>
        <tbody>
            <?php foreach ($this->points as $point) { ?>
                <tr>
                    <td><?= $point->getID(); ?></td>
                    <td><?= $point->getLat(); ?></td>
                    <td><?= $point->getLon(); ?></td>
                    <?php foreach ($rules as $rule) { ?>
                        <td>
                            <?php
                            $value = $point->getAData($rule->getID());
                            if ($rule->getType() === Field::$FREE) {
                                echo htmlentities($value);
                            } else {
                                foreach ($value as $choice => $count) {
                                    $percent = ($count / $point->getCount()) * 100.;
                                    $color = '#BBB';
                                    if ($percent >= TRUST_LIMIT) {
                                        $color = '#0B0';
                                    }
                                    echo '<span style="color: ' . $color . '">' . htmlentities($choice) . ' : ' . $percent . '%</span><br />';
                                }
                            }
                            ?>
                        </td>
                    <?php } ?>
                </tr>
            <?php } ?>
        </tbody>
    </table>
</div>