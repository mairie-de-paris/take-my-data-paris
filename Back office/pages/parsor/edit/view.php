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
    <form action="<?= Url::toDatasetImport($this->parsor); ?>" method="post">
        <label>Source : <input type="text" name="source" value="" /></label>
        <br /><br />
        <input type="submit" name="import" value="Importer des points" />
    </form>
    <br />
    "
    <?php
    $dataset = Dataset::getFromID($this->parsor);
    echo $dataset->getDesc();
    ?>
    "
    <br /><br />
    <a href="<?= Url::toPoints($this->parsor); ?>">Voir les points</a>
    <br /><br />
    <table border="1" style="border:solid black 1px; width: 100%;">
        <thead>
            <tr>
                <td>Nom</td>
                <td>Type</td>
                <td>Personnaliser</td>
            </tr>
        </thead>
        <tbody>
            <?php
            $rules = $this->current->getRules();
            $types = Field::getAll();
            foreach ($rules as $rule) {
                ?>
                <tr>
                    <td><?= $rule->getName(); ?></td>
                    <td>
                        <?php
                        foreach ($types as $type => $class) {
                            if ($class === $rule->getType()) {
                                echo $type;
                                break;
                            }
                        }
                        ?>
                    </td>
                    <td>
                        <?php
                        if ($class !== Field::$FREE) {
                            ?>
                            <a href="<?= Url::toEditParsorRule($this->parsor, $rule->getID()); ?>">Personnaliser</a>
                            <?php
                        }
                        ?>
                    </td>
                </tr>
            <?php } ?>
        </tbody>
    </table>
</div>