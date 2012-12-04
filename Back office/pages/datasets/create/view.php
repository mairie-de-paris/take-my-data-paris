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
    <form action="<?= Url::toDatasetsCreate(); ?>" method="post" enctype="multipart/form-data">
        <div id="accordion">
            <h3><a href="#">Informations g&eacute;n&eacute;ral</a></h3>
            <div>
                <label for="name">Nom du jeu</label>
                <input type="text" name="name" id="name" />
                <br /><br />
                <label for="image">Image du jeu de donn&eacute;es</label>
                <input type="file" name="image" id="image" />
                <br /><br />
                <label for="image">Pictogramme pour la carte</label>
                <input type="file" name="picto" id="picto" />
                <br /><br />
                <label for="type">Type de source</label>
                <select name="type" id="type">
                    <?php foreach (Parsor::getTypes() as $typeID => $typeName) { ?>
                        <option value="<?= $typeID; ?>"><?= $typeName ?></option>
                    <?php } ?>
                </select>

            </div>
            <h3><a href="#">Description</a></h3>
            <div>
                <label for="desc">Description du jeu de donn&eacute;es : </label><br />
                <textarea name="desc" id="desc" cols="60" rows="5"></textarea>
            </div>
            <h3><a href="#">Parseur</a></h3>
            <div>
                <div class="field">
                    <label>Nom : <input type="text" name="fieldname[]" value="" /></label>
                    <label>Description : <input type="text" name="fielddesc[]" value="" /></label>
                    <label>Type : <select name="fieldtype[]">
                            <?php
                            $list = Field::getAll();
                            foreach ($list as $name => $class) {
                                ?>
                                <option value="<?= $class; ?>"><?= $name; ?></option>
                            <?php } ?>
                        </select></label>
                </div>
                <input type="button" name="addfield" id="addfield" value="Ajouter un champ" />
            </div>
        </div>
        <br />
        <input type="submit" name="create" value="&Eacute;tape suivante" id="nextstep" />
    </form>
    <br />
</div>